package org.imgedit.webservice;

import com.google.common.primitives.Bytes;
import org.apache.log4j.Logger;
import org.imgedit.common.DirectoryScanner;
import org.imgedit.common.ImageFileProcessor;
import org.imgedit.common.ResizeImageInfo;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.multipart.*;
import org.jboss.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;


public class WebServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOG = Logger.getLogger(WebServerHandler.class.getName());

    private static final String MAIN_HTML_NAME = "./index.html";
    private static final String IMAGES_LIST_PLACE_HOLDER = "<!--ImagesListPlaceHolder-->";

    private static final String IMAGE_URI_PATH = "/image";
    private static final String UPLOAD_URI_PATH = "/upload";

    private ImageFileProcessor imageFileProcessor;
    private String baseDirectory;
    private DirectoryScanner directoryScanner;

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);


    public WebServerHandler(ImageFileProcessor imageFileProcessor, String baseDirectory) {
        this.imageFileProcessor = imageFileProcessor;
        this.baseDirectory = baseDirectory;
        this.directoryScanner = new DirectoryScanner(baseDirectory, new String[]{"jpg", "png"});
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Object msg = e.getMessage();
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uriStr = request.getUri();
            LOG.info(String.format("Handle request '%s'...", uriStr));

            HttpResponse response;
            try {
                if (request.isChunked()) {
                    // NOTE: This error should never happen if pipeline was configured properly
                    //       (HttpChunkAggregator is added)
                    throw new Exception("Chunked requests are not supported (yet?)");
                }
                String uriPath = getURIPath(uriStr);
                if (uriPath.startsWith(IMAGE_URI_PATH)) {
                    LOG.info("Return image");
                    response = getImage(uriPath.substring(IMAGE_URI_PATH.length()));
                } else if (uriPath.startsWith(UPLOAD_URI_PATH) && request.getMethod().getName().equals("POST")) {
                    LOG.info("Upload image");
                    response = uploadImage(request);
                } else {
                    LOG.info("Return main page");
                    response = getMainPage();
                }
            } catch (ClientErrorException x) {
                response = getErrorResponse(BAD_REQUEST, x.getMessage());
                LOG.warn("Client error: " + x.getMessage());
            } catch (Exception x) {
                response = getErrorResponse(INTERNAL_SERVER_ERROR, x.getMessage());
                LOG.error("Internal error: " + x.getMessage());
            }
            ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private String getURIPath(String uriStr) throws ClientErrorException {
        String decodedUriStr = uriStr;
        try {
            decodedUriStr = URLDecoder.decode(uriStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                decodedUriStr = URLDecoder.decode(uriStr, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                LOG.warn(String.format("Unable to decode uri '%s', leave it as is", uriStr));
            }
        }
        URI uri;
        try {
            uri = new URI(decodedUriStr);
        } catch (URISyntaxException e1) {
            String msg = String.format("Invalid URI '%s'.", decodedUriStr);
            LOG.error(msg);
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
            HttpPostRequestDecoder.IncompatibleDataDecoderException {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
        try {
            int width = readIntParameter(decoder, "width");
            int height = readIntParameter(decoder, "height");
            FileUpload imageFile = getFileUpload(decoder, "imagefile");
            LOG.info(String.format("Upload file %s (%sx%s)", imageFile.getFilename(), width, height));
            String imageFilePath = Paths.get(baseDirectory, imageFile.getFilename()).toString();
            imageFileProcessor.resizeImage(imageFile.getFile(), new ResizeImageInfo(width, height, imageFilePath));

            String msg = String.format("The '%s' file was uploaded. Return to the <a href=\"/\">main page</a>",
                    imageFile.getFilename());
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
            response.headers().set(CONTENT_TYPE, "text/html");
            response.setContent(ChannelBuffers.copiedBuffer(msg, CharsetUtil.UTF_8));
            response.headers().set(CONTENT_LENGTH, response.getContent().readableBytes());
            return response;
        } catch (Exception e) {
            String msg = String.format("Failed to upload the specified file: %s.<BR>Return to the <a href=\"/\">main page</a>",
                    e.getMessage());
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
            response.headers().set(CONTENT_TYPE, "text/html");
            response.setContent(ChannelBuffers.copiedBuffer(msg, CharsetUtil.UTF_8));
            response.headers().set(CONTENT_LENGTH, response.getContent().readableBytes());
            return response;
        } finally {
            decoder.cleanFiles();
        }
    }

    private HttpResponse getImage(String uriStr) throws ClientErrorException {
        HttpResponse response;
        Path filePath = Paths.get(baseDirectory, uriStr);
        LOG.info(String.format("Access the '%s' image file", uriStr));
        if (Files.exists(filePath)) {
            response = new DefaultHttpResponse(HTTP_1_1, OK);
            // Actually, we don't know the content type, so don't help browser to detect it.
            //response.headers().set(CONTENT_TYPE, "image/jpeg");
            byte[] imageFile = new byte[0];
            try {
                imageFile = Files.readAllBytes(filePath);
            } catch (IOException e) {
                throw new ClientErrorException(String.format("Unable to access file '%s'", uriStr));
            }
            response.setContent(ChannelBuffers.copiedBuffer(imageFile));
            response.headers().set(CONTENT_LENGTH, response.getContent().readableBytes());
        } else {
            response = getErrorResponse(NOT_FOUND, String.format("The '%s' file was not found!", uriStr));
        }
        return response;
    }

    private HttpResponse getMainPage() throws IOException, ClientErrorException {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "text/html");
        response.setContent(ChannelBuffers.copiedBuffer(generateMainPage()));
        response.headers().set(CONTENT_LENGTH, response.getContent().readableBytes());
        return response;
    }

    private byte[] generateMainPage() throws ClientErrorException, IOException {
        final StringBuilder strBuf = new StringBuilder();
        try {
            directoryScanner.scan(new DirectoryScanner.FileListener() {
                @Override
                public void onFile(File file) {
                    if (file.canRead()) {
                        strBuf.append(String.format("<a href=\"/image/%s\">" +
                                        "<img src=\"/image/%s\" width=\"30\" height=\"30\"> %s</a><BR>",
                                file.getName(), file.getName(), file.getName()
                        ));
                    }
                }
            });
        } catch (Exception e) {
            throw new ClientErrorException(e.getMessage());
        }

        byte[] mainPageFile = Files.readAllBytes(Paths.get(MAIN_HTML_NAME));

        byte[] result = new byte[mainPageFile.length + strBuf.length()];
        int placeholderIdx = Bytes.indexOf(mainPageFile, IMAGES_LIST_PLACE_HOLDER.getBytes());
        if (placeholderIdx != -1) {
            result = new byte[mainPageFile.length + strBuf.length()];
            System.arraycopy(mainPageFile, 0, result, 0, placeholderIdx);
            System.arraycopy(strBuf.toString().getBytes(), 0, result, placeholderIdx, strBuf.length());
            System.arraycopy(mainPageFile, placeholderIdx, result, placeholderIdx + strBuf.length(),
                    mainPageFile.length - placeholderIdx);
        } else {
            // Just do not substitute
            LOG.warn(String.format("The '%s' file does not containt the '%s' place holder. " +
                    "Images list will not be substituted", MAIN_HTML_NAME, IMAGES_LIST_PLACE_HOLDER));
        }

        return result;
    }

    private HttpResponse getErrorResponse(HttpResponseStatus status, String details) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent(ChannelBuffers.copiedBuffer(
                String.format("Failure: %s\r\nDetails: %s\r\n", status.toString(), details),
                CharsetUtil.UTF_8));
        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        LOG.error("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
