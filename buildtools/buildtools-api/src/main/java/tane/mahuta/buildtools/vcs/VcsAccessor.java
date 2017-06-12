package tane.mahuta.buildtools.vcs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Interface for <b>V</b>ersion <b>C</b>ontrol <b>S</b>ystem accessor.
 *
 * @author christian.heike@icloud.com
 *         Created on 05.06.17.
 */
public interface VcsAccessor {

    /**
     * @return the branch name
     */
    @Nullable
    String getBranch();

    /**
     * @return the revision id
     */
    @Nullable
    String getRevisionId();

    /**
     * @return a {@link Collection} of uncommitted file paths
     */
    @Nonnull
    Collection<String> getUncommittedFilePaths();

    /**
     * @return the branch configuration
     */
    @Nonnull
    VcsFlowConfig getFlowConfig();
}
