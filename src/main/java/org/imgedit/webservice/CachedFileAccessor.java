package org.imgedit.webservice;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;


@Service
@Qualifier("cached")
public class CachedFileAccessor implements FileAccessor {

    private static final Logger LOG = Logger.getLogger(CachedFileAccessor.class);

    @Autowired
    @Qualifier("simple")
    private final FileAccessor originalFileAccessor = null;

    private final Cache<Path, byte[]> cache = CacheBuilder.newBuilder().maximumSize(10).build();


    public void getFile(final Path filePath, final FileHandler fileHandler) {
        final byte[] fileContent = cache.getIfPresent(filePath);
        if (fileContent != null) {
            // It's ok to return cached value synchronously.
            LOG.info(String.format("Cache hit for the '%s' file ", filePath));
            fileHandler.onFile(fileContent);
        } else {
            LOG.info(String.format("Cache miss for the '%s' file ", filePath));
            originalFileAccessor.getFile(filePath, new FileHandler() {
                @Override
                public void onFile(byte[] content) {
                    cache.put(filePath, content);
                    fileHandler.onFile(content);
                }

                @Override
                public void onError(Throwable e) {
                    fileHandler.onError(e);
                }
            });
        }
    }

    public void invalidateFile(Path filePath) {
        LOG.info(String.format("Invalidate the '%s' file in cache ", filePath));
        cache.invalidate(filePath);
    }
}
