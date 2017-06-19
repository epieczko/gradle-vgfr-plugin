package tane.mahuta.buildtools.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Factory for {@link VersionStorage}.
 *
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
public interface VersionStorageFactory {

    /**
     * Create a version storage for the provided directory.
     * @param directory the project directory
     * @return the storage or {@code null} if none could be created
     */
    @Nullable
    VersionStorage create(@Nonnull final File directory);

}
