package org.imgedit.webservice;

import com.google.common.primitives.Bytes;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.imgedit.common.DirectoryScanner;
import org.imgedit.common.ImageFileProcessor;
import org.imgedit.common.ResizeImageInfo;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.multipart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;


@Service("webServerHandler")
@DependsOn("cli")
public class WebServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOG = Logger.getLogger(WebServerHandler.class);

    @Value("${mainpage.resource}")
    private Resource mainPageResource;

    @Value("${mainpage.placeholder}")
    private byte[] IMAGES_LIST_PLACE_HOLDER;

    @Value("${mainpage.itempattern}")
    private String IMAGE_ITEM_PATTERN;

    @Value("${uri.imageget}")
    private String IMAGE_URI_PATH = "/image";

    @Value("${uri.imageupload}")
    private String UPLOAD_URI_PATH = "/upload";

    private static final String[] EXTENSIONS_TO_SCAN = new String[]{"jpg", "png"};

    @Autowired
    @Qualifier("annotated")
    private ImageFileProcessor imageFileProcessor;

    @Autowired
    @Qualifier("cached")
    private FileAccessor fileAccessor;

    @Value("#{cli.baseDirectory}")
    private String baseDirectory;

    private DirectoryScanner directoryScanner;

    private byte[] cachedMainPage;
    private int mainPagePlaceHolderIndex = -1;

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);


    @PostConstruct
    private void initialize() {
        directoryScanner = new DirectoryScanner(baseDirectory, EXTENSIONS_TO_SCAN);
        ensureBaseDirectoryExists(baseDirectory);
        loadMainPageContent();
    }

    private void ensureBaseDirectoryExists(String baseDirectory) {
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            LOG.error(String.format("Unable to create base directory '%s'", baseDirectory));
        }
    }

    private void loadMainPageContent() {
        try {
            cachedMainPage = IOUtils.toByteArray(mainPageResource.getInputStream());
            mainPagePlaceHolderIndex = Bytes.indexOf(cachedMainPage, IMAGES_LIST_PLACE_HOLDER);
            if (mainPagePlaceHolderIndex == -1) {
                LOG.warn(String.format("The %s does not contain the '%s' place holder. Images list " +
                        "will not be substituted", mainPageResource.getDescription(),
                        new String(IMAGES_LIST_PLACE_HOLDER, "UTF-8")));
            }
        } catch (IOException e) {
            String msg = String.format("Unable to load the %s.", mainPageResource.getDescription());
            LOG.error(msg);
            cachedMainPage = msg.getBytes();
        }
    }

    @Override
    @MeasureStatistics
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Object msg = e.getMessage();
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uriStr = request.getUri();
            LOG.info(String.format("Handle request '%s'...", uriStr));

            boolean keepAlive = isKeepAlive(request);
            Channel channel = ctx.getChannel();
            HttpResponse response = null;
            try {
                if (request.isChunked()) {
                    throw new IllegalStateException("This handler should be used in pipeline " +
                            "after HttpChunkAggregator, so chunked request is not possible");
                }
                String uriPath = getURIPath(uriStr);
                if (uriPath.startsWith(IMAGE_URI_PATH)) {
                    LOG.info("Return image");
                    respondImage(uriPath.substring(IMAGE_URI_PATH.length()), channel, keepAlive);
                } else if (uriPath.startsWith(UPLOAD_URI_PATH) && request.getMethod().equals(HttpMethod.POST)) {
                    LOG.info("Upload image");
                    response = uploadImage(request);
                } else {
                    LOG.info("Return main page");
                    response = getMainPage();
                }
            } catch (ClientErrorException x) {
                response = HttpResponseBuilder.make(BAD_REQUEST).withErrorContent(x.getMessage()).build();
                LOG.warn("Client error: " + x.getMessage());
            } catch (Exception x) {
                response = HttpResponseBuilder.make(INTERNAL_SERVER_ERROR).withErrorContent(x).build();
                LOG.error("Unknown internal error", x);
            }
            if (response != null) {
                writeResponse(channel, response, keepAlive);
            }
        }
    }

    private String getURIPath(String uriStr) throws ClientErrorException {
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e1) {
            String msg = String.format("Invalid URI '%s'.", uriStr);
            throw new ClientErrorException(msg);
        }
        return uri.getPath();
    }

    private int readIntParameter(HttpPostRequestDecoder decoder, String parameterName) throws ClientErrorException,
            HttpPostRequestDecoder.NotEnoughDataDecoderException, IOException {
        InterfaceHttpData data = decoder.getBodyHttpData(parameterName);
        if (data != null && data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
            try {
                return Integer.parseInt(((Attribute) data).getValue());
            } catch (NumberFormatException e) {
                throw new ClientErrorException(
                        String.format("The '%s' parameter is expected to be integer", parameterName));
            }
        } else {
            throw new ClientErrorException(String.format("The '%s' parameter doesn't exist or has invalid format",
                    parameterName));
        }
    }

    private FileUpload getFileUpload(HttpPostRequestDecoder decoder, String parameterName) throws ClientErrorException,
            HttpPostRequestDecoder.NotEnoughDataDecoderException {
        InterfaceHttpData data = decoder.getBodyHttpData(parameterName);
        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
            return (FileUpload) data;
        } else {
            throw new ClientErrorException(String.format("The '%s' is expected to be an uploaded file", parameterName));
        }
    }

    private HttpResponse uploadImage(HttpRequest request) throws HttpPostRequestDecoder.ErrorDataDecoderException,
            HttpPostRequestDecoder.IncompatibleDataDecoderException,
            HttpPostRequestDecoder.NotEnoughDataDecoderException, IOException, ClientErrorException {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
        try {
            int width = readIntParameter(decoder, "width");
            int height = readIntParameter(decoder, "height");
            FileUpload imageFile = getFileUpload(decoder, "imagefile");
            LOG.info(String.format("Upload file %s (%sx%s)", imageFile.getFilename(), width, height));
            String imageFilePath = Paths.get(baseDirectory, imageFile.getFilename()).toString();
            imageFileProcessor.resizeImage(imageFile.getFile(), new ResizeImageInfo(width, height, imageFilePath));
            return HttpResponseBuilder.makeOk()
                .withHtml("The '%s' file was uploaded successfully. Return to the <a href=\"/\">main page</a>",
                          imageFile.getFilename())
                .build();
        } finally {
            decoder.cleanFiles();
        }
    }

    private void respondImage(final String uriStr, final Channel channel, final boolean keepAlive) throws IOException {
        LOG.info(String.format("Access the '%s' image file", uriStr));
        fileAccessor.getFile(Paths.get(baseDirectory, uriStr), new FileAccessor.FileHandler() {
            @Override
            public void onFile(byte[] content) {
                // NOTE: The content type is unknown, so don't set the "Content-Type" header.
                HttpResponse response = HttpResponseBuilder.makeOk().withContent(content).build();
                writeResponse(channel, response, keepAlive);
            }

            @Override
            public void onError(Throwable e) {
                HttpResponse response = HttpResponseBuilder
                        .make(HttpResponseStatus.NOT_FOUND)
                        .withErrorContent(String.format("Unable to access file '%s'", uriStr))
                        .build();
                writeResponse(channel, response, keepAlive);
            }
        });
    }

    private HttpResponse getMainPage() throws IOException {
        final StringBuilder strBuf = new StringBuilder();
        directoryScanner.scan(new DirectoryScanner.DefaultFileListener() {
            @Override
            public void onFile(File file) {
                if (file.canRead()) {
                    strBuf.append(String.format(IMAGE_ITEM_PATTERN, file.getName(), file.getName(), file.getName()));
                }
            }
        });

        byte[] mainPage = cachedMainPage;
        if (mainPagePlaceHolderIndex != -1) {
            mainPage = new byte[cachedMainPage.length + strBuf.length()];
            System.arraycopy(cachedMainPage, 0, mainPage, 0, mainPagePlaceHolderIndex);
            System.arraycopy(strBuf.toString().getBytes(), 0, mainPage, mainPagePlaceHolderIndex, strBuf.length());
            System.arraycopy(cachedMainPage, mainPagePlaceHolderIndex, mainPage, mainPagePlaceHolderIndex + strBuf.length(),
                    cachedMainPage.length - mainPagePlaceHolderIndex);
        }
        return HttpResponseBuilder.makeOk().withHtml(mainPage).build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        LOG.error("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }

    private void writeResponse(Channel channel, HttpResponse response, boolean keepAlive) {
        ChannelFuture writeFuture = channel.write(response);
        if (!keepAlive) {
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
