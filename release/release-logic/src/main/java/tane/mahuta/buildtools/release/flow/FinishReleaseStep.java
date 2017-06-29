package tane.mahuta.buildtools.release.flow;

import tane.mahuta.buildtools.release.AbstractGuardedReleaseStep;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;

import javax.annotation.Nonnull;

/**
 * Use the VCS flow mechanisms to finish the release branch.
 *
 * @author christian.heike@icloud.com
 *         Created on 29.06.17.
 */
public class FinishReleaseStep extends AbstractGuardedReleaseStep {

    private static class InstanceHolder {
        private static final FinishReleaseStep INSTANCE = new FinishReleaseStep();
    }

    public static FinishReleaseStep getInstance() {
        return FinishReleaseStep.InstanceHolder.INSTANCE;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Finishes the release on the VCS";
    }

    @Override
    protected void doApply(@Nonnull final ArtifactRelease release,
                           @Nonnull final ReleaseInfrastructure releaseInfrastructure,
                           @Nonnull final Object version) throws Exception {
        releaseInfrastructure.getVcs().finishReleaseBranch(version.toString());
    }
}
