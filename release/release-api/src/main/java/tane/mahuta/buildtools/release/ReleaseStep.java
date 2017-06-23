package tane.mahuta.buildtools.release;

import javax.annotation.Nonnull;

/**
 * Interface for a release step.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.06.17.
 */
public interface ReleaseStep {

    /**
     * Apply the step to the provided release using the provided infrastructure.
     *
     * @param release the artifact release
     * @param releaseInfrastructure the infrastructure for the release.
     */
    void apply(@Nonnull final ArtifactRelease release, @Nonnull final ReleaseInfrastructure releaseInfrastructure);

}
