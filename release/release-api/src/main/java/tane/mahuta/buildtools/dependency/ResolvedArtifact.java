package tane.mahuta.buildtools.dependency;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Set;

/**
 * Represents a resolved artifact.
 *
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
public interface ResolvedArtifact {

    /**
     * @return the descriptor of the
     */
    @Nonnull
    GAVCDescriptor getDescriptor();

    /**
     * @return the locale file for the resolved artifact
     */
    @Nullable
    File getLocalFile();

    /**
     * @return the dependencies of the resolved artifact
     */
    @Nonnull
    Set<ResolvedArtifact> getDependencies();

}
