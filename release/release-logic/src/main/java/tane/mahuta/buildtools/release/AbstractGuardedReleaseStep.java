package tane.mahuta.buildtools.release;

import lombok.extern.slf4j.Slf4j;
import tane.mahuta.buildtools.release.reporting.Severity;

import javax.annotation.Nonnull;

/**
 * @author christian.heike@icloud.com
 *         Created on 29.06.17.
 */
@Slf4j
public abstract class AbstractGuardedReleaseStep implements ReleaseStep {

    @Override
    public final void apply(@Nonnull final ArtifactRelease release, @Nonnull final ReleaseInfrastructure releaseInfrastructure) {
        try {
            final Object version = releaseInfrastructure.getVersionHandler().toReleaseVersion(releaseInfrastructure.getBuildToolAdapter().getVersion());
            doApply(release, releaseInfrastructure, version);
        } catch (final Exception ex) {
            log.error("Failed to run step {}", getDescription(), ex);
            release.describeProblem(b -> b
                    .severity(Severity.PROBLEM).messageFormat("Could not run release step '{}': {} {}")
                    .formatArgs(getDescription(), ex.getClass(), ex.getMessage()));
        }
    }

    /**
     * @param release               the release
     * @param releaseInfrastructure the infrastructure
     * @param version               the release version
     * @throws Exception any exception while invoking the step
     * @see ReleaseStep#apply(ArtifactRelease, ReleaseInfrastructure)
     */
    protected abstract void doApply(@Nonnull ArtifactRelease release, @Nonnull ReleaseInfrastructure releaseInfrastructure, @Nonnull final Object version) throws Exception;

}
