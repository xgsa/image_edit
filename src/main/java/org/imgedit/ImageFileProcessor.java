package org.imgedit;

import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ImageFileProcessor implements DirectoryScanner.FileListener {

    private static final Logger LOG = Logger.getLogger(ImageFileProcessor.class);

    private final ImageStreamProcessor imageStreamProcessor;


    public ImageFileProcessor(ImageStreamProcessor imageStreamProcessor) {
        this.imageStreamProcessor = imageStreamProcessor;
    }

    private String getNewImageFileName(File currentImage) {
        return currentImage.getParent() + "/scaled_" + currentImage.getName();
    }

    @Override
    public void onFile(File imageFile) {
        String imageFileName = imageFile.getName();
        LOG.info(String.format("Processing image '%s'...", imageFileName));
        InputStream inImageStream = null;
        OutputStream outImageStream = null;
        try {
            inImageStream = new FileInputStream(imageFile);
            outImageStream = new FileOutputStream(getNewImageFileName(imageFile));
            imageStreamProcessor.processImage(inImageStream, outImageStream);
        } catch (IOException e) {
            LOG.error(String.format("Error during processing image '%s': %s", imageFileName, e.getMessage()));
        } finally {
            IOUtils.closeQuietly(inImageStream);
            IOUtils.closeQuietly(outImageStream);
        }
    }
}
