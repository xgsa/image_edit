package org.imgedit;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor implements DirectoryScanner.FileListener {

    private Logger logger;


    public ImageProcessor(Logger logger) {
        this.logger = logger;
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

    private int getImageNewWidth(BufferedImage inImage) {
        return inImage.getWidth() / 2;
    }

    private int getImageNewHeight(BufferedImage inImage) {
        return inImage.getHeight() / 2;
    }

    private BufferedImage toBufferedImage(Image src) {
        BufferedImage bufImage = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufImage.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bufImage;
    }

    @Override
    public void onFile(File imageFile) {
        String imageFileName = imageFile.getName();
        logger.info(String.format("Processing image '%s'...", imageFileName));
        try {
            BufferedImage inImage = ImageIO.read(imageFile);
            int newWidth = getImageNewWidth(inImage);
            int newHeight = getImageNewHeight(inImage);
            Image outImage = inImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
            File outFile = new File(getNewImageFileName(imageFile));
            ImageIO.write(toBufferedImage(outImage), getImageFormatStr(imageFileName), outFile);
        } catch (IOException e) {
            logger.error(String.format("Error during processing image '%s': %s", imageFileName, e.getMessage()));
        }
    }
}
