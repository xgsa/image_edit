package org.imgedit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor
{

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

    public void processImage(File imageFile)
    {
        System.out.println( "Process image: " + imageFile.getName() );
        try {
            BufferedImage inImage = ImageIO.read(imageFile);
            int newWidth = getImageNewWidth(inImage);
            int newHeight = getImageNewHeight(inImage);
            Image outImage = inImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
            File outFile = new File(getNewImageFileName(imageFile));
            ImageIO.write(toBufferedImage(outImage), getImageFormatStr(imageFile.getName()), outFile);
        } catch (IOException e){
            System.out.println( "  => Error during processing image: " + e.getMessage() );
        }
    }

}
