package tane.mahuta.buildtools.release.flow;

import tane.mahuta.buildtools.release.AbstractGuardedReleaseStep;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;

import javax.annotation.Nonnull;

/**
 * Uses the VCS flow mechanism to create a release branch.
 *
 * @author christian.heike@icloud.com
 *         Created on 29.06.17.
 */
public class StartReleaseStep extends AbstractGuardedReleaseStep {

    private static class InstanceHolder {
        private static final StartReleaseStep INSTANCE = new StartReleaseStep();
    }

    public static StartReleaseStep getInstance() {
        return StartReleaseStep.InstanceHolder.INSTANCE;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Starts the release with on the VCS";
    }

    @Override
    protected void doApply(@Nonnull final ArtifactRelease release,
                           @Nonnull final ReleaseInfrastructure releaseInfrastructure,
                           @Nonnull final Object releaseVersion) throws Exception {
        releaseInfrastructure.getVcs().startReleaseBranch(releaseVersion.toString());
    }
}
