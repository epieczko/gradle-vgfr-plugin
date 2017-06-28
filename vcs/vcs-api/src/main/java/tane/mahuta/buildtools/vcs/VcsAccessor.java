package tane.mahuta.buildtools.vcs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

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

    /**
     * Start the release branch, if not already on one.
     *
     * @param version the release version
     */
    void startReleaseBranch(@Nonnull final String version);

    /**
     * Finish the release branch.
     *
     * @param version the release version
     * @return {@code true} if no problems occured, {@code false}
     */
    boolean finishReleaseBranch(@Nonnull final String version);

    /**
     * Commits all changed files.
     * @param message the message for the commit
     */
    void commitFiles(@Nonnull final String message);

    /**
     * Pushes the changes to the remote.
     */
    void push();

    /**
     * Checks out the provided branch.
     *
     * @param branch the branch to be checked out
     */
    void checkout(@Nonnull String branch);

    /**
     * Removes the branch prefix from the current branch.
     * If the branch is not prefixed with the provided one, will return {@code null}.
     *
     * @param prefix the branch prefix to be removed
     * @return the branch without the prefix or {@code null} if the branch is {@code null} or the prefix did not match
     */
    @Nullable
    default String removeBranchPrefix(@Nonnull final String prefix) {
        return isOnBranch(prefix)
                ? getBranch().substring(prefix.length())
                : null;
    }

    /**
     * Checks if the current branch is equal or prefixed with the provided value.
     *
     * @param prefixOrBranchName the branch name or prefix to check
     * @return {@code true} if the prefix or branch name matched, {@code false} otherwise
     */
    default boolean isOnBranch(@Nonnull final String prefixOrBranchName) {
        return Optional.ofNullable(getBranch())
                .map(b -> b.startsWith(prefixOrBranchName))
                .orElse(false);
    }
}
