package org.imgedit.rawservice;

import org.apache.log4j.Logger;
import org.imgedit.common.Env;
import org.imgedit.common.ImageStreamProcessor;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


public class Server extends Env {

    private static final int PORT = 8080;
    private static final int MAX_FRAME_SIZE = 10*1024*1024;  // 10 Mb seems quite enough

    private static final Logger LOG = Logger.getLogger(Server.class);


    public static void run() {
        final ImageStreamProcessor imageStreamProcessor = new ImageStreamProcessor();

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
                        new LengthFieldBasedFrameDecoder(MAX_FRAME_SIZE, 0, 4, 0, 4),
                        new ImageServerHandler(imageStreamProcessor)
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
