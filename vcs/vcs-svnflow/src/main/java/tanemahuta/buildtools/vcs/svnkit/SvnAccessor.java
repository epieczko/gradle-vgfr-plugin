package tanemahuta.buildtools.vcs.svnkit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tmatesoft.svn.core.wc.SVNBasicClient;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author christian.heike@icloud.com
 *         Created on 12.07.17.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SvnAccessor implements VcsAccessor {

    private final SVNBasicClient client;

    @Nullable
    @Override
    public String getBranch() {
        return null;
    }

    @Nullable
    @Override
    public String getRevisionId() {
        return null;
    }

    @Nonnull
    @Override
    public Collection<String> getUncommittedFilePaths() {
        return null;
    }

    @Nonnull
    @Override
    public VcsFlowConfig getFlowConfig() {
        return null;
    }

    @Override
    public void startReleaseBranch(@Nonnull String version) {

    }

    @Override
    public boolean finishReleaseBranch(@Nonnull String version) {
        return false;
    }

    @Override
    public void commitFiles(@Nonnull String message) {

    }

    @Override
    public void push() {

    }

    @Override
    public void checkout(@Nonnull String branch) {

    }

    @Override
    public void pushTags() {

    }
}
