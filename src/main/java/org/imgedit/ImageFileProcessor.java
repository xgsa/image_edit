package org.imgedit;

import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ImageFileProcessor implements DirectoryScanner.FileListener {

    private final Logger logger;
    private final ImageStreamProcessor imageStreamProcessor;


    public ImageFileProcessor(Logger logger, ImageStreamProcessor imageStreamProcessor) {
        this.logger = logger;
        this.imageStreamProcessor = imageStreamProcessor;
    }

    private String getImageFormatStr(String imageFileName) {
        if (imageFileName.toLowerCase().endsWith("png")) {
            return "png";
        } else {
            return "jpg";
        }
    }

    private String getNewImageFileName(File currentImage) {
        return currentImage.getParent() + "/scaled_" + currentImage.getName();
    }

    @Override
    public void onFile(File imageFile) {
        String imageFileName = imageFile.getName();
        logger.info(String.format("Processing image '%s'...", imageFileName));
        InputStream inImageStream = null;
        OutputStream outImageStream = null;
        try {
            inImageStream = new FileInputStream(imageFile);
            outImageStream = new FileOutputStream(getNewImageFileName(imageFile));
            imageStreamProcessor.processImage(inImageStream, outImageStream, getImageFormatStr(imageFileName));
        } catch (IOException e) {
            logger.error(String.format("Error during processing image '%s': %s", imageFileName, e.getMessage()));
        } finally {
            IOUtils.closeQuietly(inImageStream);
            IOUtils.closeQuietly(outImageStream);
        }
    }
}
