package org.imgedit.webservice;

import java.io.IOException;
import java.nio.file.Path;


public interface FileAccessor {

    public byte[] getFile(Path filePath) throws IOException;

}
