package tane.mahuta.buildtools.release.flow;

import tane.mahuta.buildtools.release.AbstractGuardedReleaseStep;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;

import javax.annotation.Nonnull;

/**
 * Runs the release build via the {@link tane.mahuta.buildtools.release.BuildToolAdapter}.
 *
 * @author christian.heike@icloud.com
 *         Created on 29.06.17.
 */
public class RunReleaseBuildStep extends AbstractGuardedReleaseStep {

    private static class InstanceHolder {
        private static final RunReleaseBuildStep INSTANCE = new RunReleaseBuildStep();
    }

    public static RunReleaseBuildStep getInstance() {
        return RunReleaseBuildStep.InstanceHolder.INSTANCE;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Runs the release build and uploads the artifacts";
    }

    @Override
    protected void doApply(@Nonnull final ArtifactRelease release,
                           @Nonnull final ReleaseInfrastructure releaseInfrastructure,
                           @Nonnull final Object releaseVersion) throws Exception {
        releaseInfrastructure.getBuildToolAdapter().buildRelease();
    }
}
