package org.imgedit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

public class App {

    private static final String logPropertiesFilename = "log4j.properties";
    private static final int widthRatio = 2;
    private static final int heightRatio = 2;


    private static Logger getLogger() {
        return Logger.getLogger(App.class);
    }

    private static void configureLogging() {
        File logProperties = new File(logPropertiesFilename);
        String logSourceStr;
        if (logProperties.exists()) {
            PropertyConfigurator.configure(logPropertiesFilename);
            logSourceStr = String.format("from the '%s' file", logPropertiesFilename);
        } else {
            BasicConfigurator.configure();
            logSourceStr = "by default";
        }
        getLogger().info(String.format("Hi! The application was started, logging was configured %s!", logSourceStr));
    }

    private static void processDirectory(String directoryPath) {
        try {
            DirectoryScanner directoryScanner = new DirectoryScanner(directoryPath);
            ImageStreamProcessor imageStreamProcessor = new ImageStreamProcessor(widthRatio, heightRatio);
            Logger imageFileProcessorLog = Logger.getLogger(ImageFileProcessor.class);
            ImageFileProcessor imageFileProcessor = new ImageFileProcessor(imageFileProcessorLog, imageStreamProcessor);
            directoryScanner.scan(imageFileProcessor);
        } catch (Exception e) {
            getLogger().error(String.format("Error: %s.", e.getMessage()));
        }
    }

    public static void main(String[] args) {
        configureLogging();
        if (args.length >= 1) {
            processDirectory(args[0]);
        } else {
            getLogger().error("Error: Please specify the directory with images to scan.");
        }
    }
}
