package tane.mahuta.buildtools.version.storage;

import tane.mahuta.buildtools.version.VersionStorageFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Implementation of {@link VersionStorageFactory} for {@code gradle.properties} file.
 *
 * @author christian.heike@icloud.com
 *         Created on 28.05.17.
 */
public class GradlePropertiesProjectVersionStorageFactory extends AbstractPropertyProjectVersionStorageFactory {
    @Override
    @Nullable
    protected File propertyVersionFileIn(@Nonnull final File directory) {
        return new File(directory, "gradle.properties");
    }

    @Override
    protected String propertyName() {
        return "version";
    }

}
