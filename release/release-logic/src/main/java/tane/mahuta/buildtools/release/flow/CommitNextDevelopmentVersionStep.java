package tane.mahuta.buildtools.release.flow;

import tane.mahuta.buildtools.release.AbstractGuardedReleaseStep;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;

import javax.annotation.Nonnull;

/**
 * Commits the next development iteration version.
 *
 * @author christian.heike@icloud.com
 *         Created on 29.06.17.
 */
public class CommitNextDevelopmentVersionStep extends AbstractGuardedReleaseStep {

    private static class InstanceHolder {
        private static final CommitNextDevelopmentVersionStep INSTANCE = new CommitNextDevelopmentVersionStep();
    }

    public static CommitNextDevelopmentVersionStep getInstance() {
        return CommitNextDevelopmentVersionStep.InstanceHolder.INSTANCE;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Set, store and commit the next development iteration version";
    }

    @Override
    protected void doApply(@Nonnull final ArtifactRelease release,
                           @Nonnull final ReleaseInfrastructure releaseInfrastructure,
                           @Nonnull final Object releaseVersion) throws Exception {

        releaseInfrastructure.getVcs().checkout(releaseInfrastructure.getVcs().getFlowConfig().getDevelopmentBranch());

        final Object nextVersion = releaseInfrastructure.getVersionHandler().toNextDevelopmentVersion(releaseVersion);
        releaseInfrastructure.getBuildToolAdapter().setVersion(nextVersion);
        releaseInfrastructure.getVersionStorage().store(nextVersion.toString());
        releaseInfrastructure.getVcs().commitFiles("Next development iteration version: " + nextVersion);
        releaseInfrastructure.getVcs().push();
    }
}
