package org.imgedit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

    private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
    private static final int WIDTH_RATIO = 2;
    private static final int HEIGHT_RATIO = 2;
    private static final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "png"};

    private static final int THREADS_IN_POOL = Runtime.getRuntime().availableProcessors();
    private static final int TIMEOUT_VALUE = 1;


    private static Logger getLogger() {
        return Logger.getLogger(App.class);
    }

    private static void configureLogging() {
        File logProperties = new File(LOG_PROPERTIES_FILENAME);
        String logSourceStr;
        if (logProperties.exists()) {
            PropertyConfigurator.configure(LOG_PROPERTIES_FILENAME);
            logSourceStr = String.format("from the '%s' file", LOG_PROPERTIES_FILENAME);
        } else {
            BasicConfigurator.configure();
            logSourceStr = "by default";
        }
        getLogger().info(String.format("Hi! The application was started, logging was configured %s!", logSourceStr));
    }

    private static void processDirectory(String directoryPath) {
        try {
            DirectoryScanner directoryScanner = new DirectoryScanner(directoryPath, IMAGE_EXTENSIONS);
            ImageStreamProcessor imageStreamProcessor = new ImageStreamProcessor(WIDTH_RATIO, HEIGHT_RATIO);
            Logger imageFileProcessorLog = Logger.getLogger(ImageFileProcessor.class);
            ImageFileProcessor imageFileProcessor = new ImageFileProcessor(imageFileProcessorLog, imageStreamProcessor);
            getLogger().info(String.format("Run in parallel mode (with %s threads).", THREADS_IN_POOL));
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS_IN_POOL);
            ParallelFileProcessor parallelFileProcessor = new ParallelFileProcessor(executorService, imageFileProcessor);
            directoryScanner.scan(parallelFileProcessor);
            executorService.shutdown();
            boolean terminatedOk = executorService.awaitTermination(TIMEOUT_VALUE, TimeUnit.HOURS);
            if (!terminatedOk) {
                getLogger().error("Execution of parallel subtasks was terminated by timeout.");
            } else {
                getLogger().info("Done.");
            }
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
