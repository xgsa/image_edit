package org.imgedit.common;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;

public class DirectoryScanner {

    private static final Logger LOG = Logger.getLogger(DirectoryScanner.class);

    private String directoryPath = ".";
    private String[] extensions;


    public static interface FileListener {
        public void onScanStarted();

        public void onFile(File file);

        public void onScanFinished();
    }

    public static class DefaultFileListener implements FileListener {
        @Override
        public void onScanStarted() {}

        @Override
        public void onFile(File file) {}

        @Override
        public void onScanFinished() {}
    }


    private class ImageFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            if (extensions == null) {
                return true;
            }
            String lowName = name.toLowerCase();
            for (String extension : extensions) {
                if (lowName.endsWith(extension)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final FilenameFilter imageFilenameFilter = new ImageFilenameFilter();


    public DirectoryScanner() {
    }

    public DirectoryScanner(String directoryPath, String[] extensions) {
        this.directoryPath = directoryPath;
        this.extensions = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            this.extensions[i] = extensions[i].toLowerCase();
        }
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    public void scan(FileListener fileListener) {
        File imagesDirectory = new File(directoryPath);
        if (imagesDirectory.exists() && imagesDirectory.isDirectory()) {
            fileListener.onScanStarted();
            for (File imageFile : imagesDirectory.listFiles(imageFilenameFilter)) {
                fileListener.onFile(imageFile);
            }
            fileListener.onScanFinished();
        } else {
            // Yep, more specific exception should be thrown here
            LOG.error(String.format("The '%s' path does not exist or is not a directory!", directoryPath));
        }
    }
}
