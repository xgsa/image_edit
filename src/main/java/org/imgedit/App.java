package org.imgedit;

public class App {
    private static void processDirectory(String directoryPath) {
        try {
            DirectoryScanner directoryScanner = new DirectoryScanner(directoryPath);
            ImageProcessor imageProcessor = new ImageProcessor();
            directoryScanner.scan(imageProcessor);
        } catch (Exception e) {
            System.err.println(String.format("Error: %s.", e.getMessage()));
        }
    }

    public static void main(String[] args) {
        if (args.length >= 1) {
            processDirectory(args[0]);
        } else {
            System.err.println("Error: Please specify the directory with images to scan.");
        }
    }
}
