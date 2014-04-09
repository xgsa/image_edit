package org.imgedit.webservice;

import org.apache.log4j.Logger;
import org.imgedit.common.Env;
import org.imgedit.common.ImageFileProcessor;
import org.imgedit.common.ImageStreamProcessor;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


public class WebServer extends Env {

    private static final String BASE_DIRECTORY = ".";
    private static final int PORT = 8080;

    private static final int MAX_FRAME_SIZE = 10*1024*1024;  // 10 Mb seems quite enough

    private static final Logger LOG = Logger.getLogger(WebServer.class);


    public static void run() {
        final ImageStreamProcessor imageStreamProcessor = new ImageStreamProcessor();
        final ImageFileProcessor imageFileProcessor = new ImageFileProcessor(imageStreamProcessor);

        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        // NOTE: Add "new LoggingHandler(InternalLogLevel.ERROR)," for debug.
                        new HttpRequestDecoder(),
                        new HttpChunkAggregator(MAX_FRAME_SIZE),
                        new HttpResponseEncoder(),
                        new HttpContentCompressor(),
                        new WebServerHandler(imageFileProcessor, BASE_DIRECTORY)
                );
            }
        });

        // Bind and start to accept incoming connections.
        LOG.info(String.format("Listening on %s...", PORT));
        bootstrap.bind(new InetSocketAddress(PORT));
    }

    public static void main(String[] args) {
        configureLogging();
        run();
    }
}
