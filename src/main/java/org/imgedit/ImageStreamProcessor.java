package org.imgedit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageStreamProcessor {

    private int widthRatio, heightRatio;


    public ImageStreamProcessor(int widthRatio, int heightRatio) {
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
    }

    private int getImageNewWidth(BufferedImage inImage) {
        return inImage.getWidth() / widthRatio;
    }

    private int getImageNewHeight(BufferedImage inImage) {
        return inImage.getHeight() / heightRatio;
    }

    private BufferedImage toBufferedImage(Image src) {
        BufferedImage bufImage = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufImage.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bufImage;
    }

    public void processImage(InputStream inputImage, OutputStream outputImage, String formatName) throws IOException {
        BufferedImage inImage = ImageIO.read(inputImage);
        int newWidth = getImageNewWidth(inImage);
        int newHeight = getImageNewHeight(inImage);
        Image outImage = inImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        ImageIO.write(toBufferedImage(outImage), formatName, outputImage);
    }
}
