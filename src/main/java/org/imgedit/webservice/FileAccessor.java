package org.imgedit.webservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileAccessor {

    public byte[] getFile(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }
}
