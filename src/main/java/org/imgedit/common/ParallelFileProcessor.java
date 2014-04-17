package org.imgedit.common;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ParallelFileProcessor implements DirectoryScanner.FileListener {

    private static final Logger LOG = Logger.getLogger(ParallelFileProcessor.class);

    private final ExecutorService executorService;
    private final DirectoryScanner.FileListener clientListener;

    private long timeoutValue = 1;
    private TimeUnit timeoutUnits = TimeUnit.HOURS;
    private long startTime;


    public ParallelFileProcessor(ExecutorService executorService, DirectoryScanner.FileListener clientListener) {
        this.executorService = executorService;
        this.clientListener = clientListener;
    }

    public void setTimeoutValue(long timeoutValue) {
        this.timeoutValue = timeoutValue;
    }

    public void setTimeoutUnits(TimeUnit timeoutUnits) {
        this.timeoutUnits = timeoutUnits;
    }

    @Override
    public void onScanStarted() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onFile(final File file) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                clientListener.onFile(file);
            }
        });
    }

    @Override
    public void onScanFinished() {
        try {
            executorService.shutdown();
            boolean terminatedOk = executorService.awaitTermination(timeoutValue, timeoutUnits);
            if (!terminatedOk) {
                LOG.error(String.format("Execution of parallel subtasks was terminated by timeout after %s sec.",
                        getElapsedTime()));
            } else {
                LOG.info(String.format("Done in %s sec.", getElapsedTime()));
            }
        } catch (InterruptedException e) {
            LOG.warn(String.format("Interrupted after %s sec.", getElapsedTime()));
        }
    }

    private double getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }
}
