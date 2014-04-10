package org.imgedit.webservice;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;


public class CachedFileAccessor extends FileAccessor {

    private static final Logger LOG = Logger.getLogger(CachedFileAccessor.class);

    private FileAccessor originalFileAccessor;
    private LoadingCache<Path, byte[]> cache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .build(
                new CacheLoader<Path, byte[]>() {
                    public byte[] load(Path key) throws IOException {
                        LOG.info(String.format("Cache miss for the '%s' file ", key));
                        return originalFileAccessor.getFile(key);
                    }
                }
            );

    public CachedFileAccessor(FileAccessor originalFileAccessor) {
        this.originalFileAccessor = originalFileAccessor;
    }


    public byte[] getFile(Path filePath) throws IOException {
        try {
            LOG.info(String.format("Access the '%s' file via cache ", filePath));
            return cache.get(filePath);
        } catch (ExecutionException e) {
            throw (IOException) e.getCause();
        }
    }

    public void invalidateFile(Path filePath) {
        LOG.info(String.format("Invalidate the '%s' file in cache ", filePath));
        cache.invalidate(filePath);
    }
}
