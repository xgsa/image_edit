package org.imgedit.application;

import org.imgedit.common.DirectoryScanner;
import org.imgedit.common.ImageFileProcessor;
import org.imgedit.common.ResizeImageInfo;

import java.io.File;


class ImageFilesProcessingListener extends DirectoryScanner.DefaultFileListener {

    private final ImageFileProcessor imageFileProcessor;

    private int resultWidth = 100;
    private int resultHeight = 100;
    private String resultFilenamePattern = "scaled_%s";


    public ImageFilesProcessingListener(ImageFileProcessor imageFileProcessor) {
        this.imageFileProcessor = imageFileProcessor;
    }

    @Override
    public void onFile(File file) {
        String newFileName = file.getParent() + "/" + String.format(resultFilenamePattern, file.getName());
        imageFileProcessor.resizeImage(file, new ResizeImageInfo(resultWidth, resultHeight, newFileName));
    }

    public void setResultWidth(int resultWidth) {
        this.resultWidth = resultWidth;
    }

    public void setResultHeight(int resultHeight) {
        this.resultHeight = resultHeight;
    }

    public void setResultFilenamePattern(String resultFilenamePattern) {
        this.resultFilenamePattern = resultFilenamePattern;
    }
}
