package org.imgedit.common;

import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ImageFileProcessor {

    private static final Logger LOG = Logger.getLogger(ImageFileProcessor.class);

    private final ImageStreamProcessor imageStreamProcessor;


    public ImageFileProcessor(ImageStreamProcessor imageStreamProcessor) {
        this.imageStreamProcessor = imageStreamProcessor;
    }

    public void resizeImage(File imageFile, ResizeImageInfo imageInfo) {
        String imageFileName = imageFile.getName();
        LOG.info(String.format("Processing image '%s'...", imageFileName));
        InputStream inImageStream = null;
        OutputStream outImageStream = null;
        try {
            inImageStream = new FileInputStream(imageFile);
            outImageStream = new FileOutputStream(imageInfo.getName());
            imageStreamProcessor.resizeImage(inImageStream, outImageStream, imageInfo);
        } catch (IOException e) {
            LOG.error(String.format("Error during processing image '%s': %s", imageFileName, e.getMessage()));
        } finally {
            IOUtils.closeQuietly(inImageStream);
            IOUtils.closeQuietly(outImageStream);
        }
    }
}
