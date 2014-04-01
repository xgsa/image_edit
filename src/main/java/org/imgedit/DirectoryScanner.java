package org.imgedit;


import java.io.File;
import java.io.FilenameFilter;

public class DirectoryScanner
{
    private String directoryPath;


    public DirectoryScanner(String directoryPath)
    {
        this.directoryPath = directoryPath;
    }

    public void scan(ImageProcessor imageProcessor) throws Exception {
        File imagesDirectory = new File(directoryPath);
        if (imagesDirectory.isDirectory()) {
            File[] imageFiles = imagesDirectory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String lowName = name.toLowerCase();
                    return lowName.endsWith(".jpg") || lowName.endsWith(".png");
                }
            });
            for (File imageFile : imageFiles) {
                imageProcessor.processImage(imageFile);
            }
        } else {
            // Yep, more specific exception should be thrown here
            throw new Exception(String.format("The '%s' path does not exist or is not a directory!", directoryPath));
        }
    }
}
