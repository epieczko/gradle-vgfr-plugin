package tane.mahuta.buildtools.vcs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Factory for {@link VcsAccessor}s.
 *
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
public interface VcsAccessorFactory {

    /**
     * Create a new {@link VcsAccessor} for the provided directory.
     * @param directory the directory
     * @return the accessor created for the project
     */
    @Nullable
    VcsAccessor create(@Nonnull final File directory);

}
