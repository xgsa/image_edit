package org.imgedit.webservice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class SimpleFileAccessor implements FileAccessor {

    public byte[] getFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        } else {
            throw new FileNotFoundException(String.format("The '%s' file was not found!", filePath));
        }
    }
}
