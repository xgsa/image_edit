package org.imgedit.webservice;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;


public class SimpleFileAccessor implements FileAccessor {


    private static class ChannelWriterCompletionHandler implements CompletionHandler<Integer, FileHandler> {

        private final byte[] buffer;

        private ChannelWriterCompletionHandler(byte[] buffer) {
            this.buffer = buffer;
        }

        @Override
        public void completed(Integer result, FileHandler fileHandler) {
            fileHandler.onFile(buffer);
        }

        @Override
        public void failed(Throwable e, FileHandler fileHandler) {
            fileHandler.onError(e);
        }
    }

    public void getFile(Path filePath, FileHandler fileHandler) {
        try {
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(filePath);
            byte[] buffer = new byte[(int) fileChannel.size()];
            fileChannel.read(ByteBuffer.wrap(buffer), 0, fileHandler, new ChannelWriterCompletionHandler(buffer));
        } catch (IOException e) {
            fileHandler.onError(e);
        }
    }
}
