package org.imgedit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class ImageStreamProcessor {

    private BufferedImage toBufferedImage(Image src) {
        BufferedImage bufImage = new BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufImage.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bufImage;
    }

    public boolean resizeImage(InputStream inputImage, OutputStream outputImage, ResizeImageInfo imageInfo)
            throws IOException {
        ImageInputStream stream = ImageIO.createImageInputStream(inputImage);
        Iterator iter = ImageIO.getImageReaders(stream);
        if (!iter.hasNext()) {
            return false;
        }
        ImageReader reader = (ImageReader) iter.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(stream, true, true);
        BufferedImage inImage;
        try {
            inImage = reader.read(0, param);
        } finally {
            reader.dispose();
            stream.close();
        }
        if (inImage == null) {
            stream.close();
            return false;
        }
        Image outImage = inImage.getScaledInstance(imageInfo.getWidth(), imageInfo.getHeight(), Image.SCALE_DEFAULT);
        ImageIO.write(toBufferedImage(outImage), reader.getFormatName(), outputImage);
        return true;
    }
}
