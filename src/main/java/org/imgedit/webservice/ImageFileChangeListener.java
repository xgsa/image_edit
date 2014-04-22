package org.imgedit.webservice;

import org.imgedit.common.ImageFileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;


@Component
class ImageFileChangeListener implements ImageFileProcessor.FileChangeListener {

    @Autowired
    private CachedFileAccessor cachedFileAccessor;


    @Override
    public void onFileChange(String filePath) {
        cachedFileAccessor.invalidateFile(Paths.get(filePath));
    }
}
