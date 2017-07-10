package tane.mahuta.buildtools.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Interface for a parser for a version.
 *
 * @param <V> the concrete type of the parsed version
 * @author christian.heike@icloud.com
 *         Created on 04.06.17.
 */
public interface VersionParser<V> {

    /**
     * Parse the provided source to a version.
     *
     * @param source          the source to be parsed
     * @param sourceDirectory the source directory of the project
     * @return the parsed version
     */
    @Nonnull
    V parse(@Nonnull String source, @Nullable File sourceDirectory);

}
