package org.imgedit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import static org.jboss.netty.buffer.ChannelBuffers.copiedBuffer;


public class ImageServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger LOG = Logger.getLogger(ImageServerHandler.class.getName());

    private ImageStreamProcessor imageStreamProcessor;


    public ImageServerHandler(ImageStreamProcessor imageStreamProcessor) {
        this.imageStreamProcessor = imageStreamProcessor;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        ChannelBuffer inChannelBuffer = (ChannelBuffer) e.getMessage();
        int availableBytes = inChannelBuffer.readableBytes();
        LOG.info(String.format("Got image (%s bytes)", availableBytes));
        byte[] internalBuffer = new byte[availableBytes];
        inChannelBuffer.readBytes(internalBuffer);
        InputStream inputStream = new ByteArrayInputStream(internalBuffer);
        // NOTE: Allocating buffer of availableBytes length allocates extra memory. but avoid memory relocation.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(availableBytes);
        try {
            imageStreamProcessor.resizeImage(inputStream, outputStream, new ResizeImageInfo(100, 100));
        } catch (IOException x) {
            LOG.error("Unexpected exception during of processing image.", x.getCause());
        }
        // NOTE: It is not very optimal that the result buffer is copied twice:
        //       once - in toByteArray() and once again - in copiedBuffer().
        e.getChannel().write(copiedBuffer(outputStream.toByteArray()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        LOG.error("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
