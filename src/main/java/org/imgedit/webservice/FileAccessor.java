package org.imgedit.webservice;

import java.nio.file.Path;


public interface FileAccessor {

    public static interface FileHandler {
        public void onFile(byte[] content);

        public void onError(Throwable e);
    }

    public void getFile(Path filePath, FileHandler fileHandler);

}
