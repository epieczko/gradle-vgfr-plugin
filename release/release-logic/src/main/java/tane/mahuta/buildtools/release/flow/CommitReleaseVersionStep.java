package tane.mahuta.buildtools.release.flow;

import tane.mahuta.buildtools.release.AbstractGuardedReleaseStep;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;

import javax.annotation.Nonnull;

/**
 * Commits the release version in the release branch.
 *
 * @author christian.heike@icloud.com
 *         Created on 29.06.17.
 */
public class CommitReleaseVersionStep extends AbstractGuardedReleaseStep {

    private static class InstanceHolder {
        private static final CommitReleaseVersionStep INSTANCE = new CommitReleaseVersionStep();
    }

    public static CommitReleaseVersionStep getInstance() {
        return CommitReleaseVersionStep.InstanceHolder.INSTANCE;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Set, store and commit the release version on the project";
    }

    @Override
    protected void doApply(@Nonnull final ArtifactRelease release,
                           @Nonnull final ReleaseInfrastructure releaseInfrastructure,
                           @Nonnull final Object version) throws Exception {
        final String versionString = version.toString();
        releaseInfrastructure.getBuildToolAdapter().setVersion(version);
        releaseInfrastructure.getVersionStorage().store(versionString);
        releaseInfrastructure.getVcs().commitFiles("Set release version " + versionString);
        releaseInfrastructure.getVcs().push();
    }
}
