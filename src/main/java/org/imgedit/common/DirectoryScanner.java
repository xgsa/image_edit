package org.imgedit.common;


import java.io.File;
import java.io.FilenameFilter;

public class DirectoryScanner {
    private final String directoryPath;
    private final String[] extensions;


    public static interface FileListener {
        public void onFile(File file);
    }


    private class ImageFilenameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
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


    public DirectoryScanner(String directoryPath, String[] extensions) {
        this.directoryPath = directoryPath;
        this.extensions = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            this.extensions[i] = extensions[i].toLowerCase();
        }
    }

    public void scan(FileListener fileListener) throws Exception {
        File imagesDirectory = new File(directoryPath);
        if (imagesDirectory.exists() && imagesDirectory.isDirectory()) {
            for (File imageFile : imagesDirectory.listFiles(imageFilenameFilter)) {
                fileListener.onFile(imageFile);
            }
        } else {
            // Yep, more specific exception should be thrown here
            throw new Exception(String.format("The '%s' path does not exist or is not a directory!", directoryPath));
        }
    }
}
