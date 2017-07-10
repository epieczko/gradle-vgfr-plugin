package tane.mahuta.buildtools.version.storage;

import lombok.extern.slf4j.Slf4j;
import tane.mahuta.buildtools.version.VersionStorage;
import tane.mahuta.buildtools.version.VersionStorageFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Optional;

/**
 * Abstract {@link VersionStorageFactory} for property based files.
 *
 * @author christian.heike@icloud.com
 *         Created on 28.05.17.
 */
@Slf4j
public abstract class AbstractPropertyProjectVersionStorageFactory implements VersionStorageFactory {
    /**
     * Get the file representation of the target file in the provided directory.
     *
     * @param directory the directory
     * @return the file representing the version storage in the directory
     */
    @Nullable
    protected abstract File propertyVersionFileIn(@Nonnull final File directory);

    /**
     * @return the name of the property in the file
     */
    @Nonnull
    protected abstract String propertyName();

    @Override
    public VersionStorage create(@Nonnull final File directory) {
        return Optional.ofNullable(findPropertyFile(directory)).map(f -> new PropertyVersionStorage(f, propertyName())).orElse(null);
    }

    @Nullable
    private File findPropertyFile(@Nonnull final File directory) {
        File curDir = directory;
        while (curDir != null) {
            final File result = propertyVersionFileIn(curDir).getAbsoluteFile();
            if (result.isFile()) {
                log.debug("Found version property file: {}", result);
                return result;
            }

            curDir = curDir.getParentFile();
        }

        log.debug("Could not find property file recursively upwards in: {}", directory);

        return null;
    }

}
