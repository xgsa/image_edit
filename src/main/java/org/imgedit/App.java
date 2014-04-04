package org.imgedit;

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App extends Env {

    private static final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "png"};

    private static final int THREADS_IN_POOL = Runtime.getRuntime().availableProcessors();
    private static final int TIMEOUT_VALUE = 1;

    private static final Logger LOG = Logger.getLogger(App.class);


    private static void processDirectory(String directoryPath) {
        try {
            long startTime = System.currentTimeMillis();
            DirectoryScanner directoryScanner = new DirectoryScanner(directoryPath, IMAGE_EXTENSIONS);
            ImageStreamProcessor imageStreamProcessor = new ImageStreamProcessor(WIDTH_RATIO, HEIGHT_RATIO);
            ImageFileProcessor imageFileProcessor = new ImageFileProcessor(imageStreamProcessor);
            LOG.info(String.format("Run in parallel mode (with %s threads).", THREADS_IN_POOL));
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS_IN_POOL);
            ParallelFileProcessor parallelFileProcessor = new ParallelFileProcessor(executorService, imageFileProcessor);
            directoryScanner.scan(parallelFileProcessor);
            executorService.shutdown();
            boolean terminatedOk = executorService.awaitTermination(TIMEOUT_VALUE, TimeUnit.HOURS);
            if (!terminatedOk) {
                LOG.error("Execution of parallel subtasks was terminated by timeout.");
            } else {
                LOG.info(String.format("Done in %s sec.", (System.currentTimeMillis() - startTime) / 1000.0));
            }
        } catch (Exception e) {
            LOG.error(String.format("Error: %s.", e.getMessage()));
        }
    }

    public static void main(String[] args) {
        configureLogging();
        if (args.length >= 1) {
            processDirectory(args[0]);
        } else {
            LOG.error("Error: Please specify the directory with images to scan.");
        }
    }
}
