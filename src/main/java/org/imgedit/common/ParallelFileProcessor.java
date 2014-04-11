package org.imgedit.common;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class ParallelFileProcessor implements DirectoryScanner.FileListener {

    private final ExecutorService executorService;
    private final DirectoryScanner.FileListener clientListener;


    public ParallelFileProcessor(ExecutorService executorService, DirectoryScanner.FileListener clientListener) {
        this.executorService = executorService;
        this.clientListener = clientListener;
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
}