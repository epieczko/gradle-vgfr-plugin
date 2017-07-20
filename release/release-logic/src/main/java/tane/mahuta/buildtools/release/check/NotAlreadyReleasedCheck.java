package tane.mahuta.buildtools.release.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tane.mahuta.buildtools.dependency.Artifact;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor;
import tane.mahuta.buildtools.release.AbstractGuardedReleaseStep;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;
import tane.mahuta.buildtools.release.reporting.Severity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Checks if the artifact was not already released and uploaded.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.06.17.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotAlreadyReleasedCheck extends AbstractGuardedReleaseStep {

    private static class InstanceHolder {
        private static final NotAlreadyReleasedCheck INSTANCE = new NotAlreadyReleasedCheck();
    }

    public static final NotAlreadyReleasedCheck getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected void doApply(@Nonnull final ArtifactRelease release,
                           @Nonnull final ReleaseInfrastructure releaseInfrastructure,
                           @Nonnull final Object releaseVersion) {

        final GAVCDescriptor currentDescriptor = release.getDescriptor();

        final GAVCDescriptor releaseDescriptor = DefaultGAVCDescriptor.builder()
                .group(currentDescriptor.getGroup())
                .artifact(currentDescriptor.getArtifact())
                .version(releaseVersion.toString()).build();

        final Artifact releasedArtifact = releaseInfrastructure.getArtifactResolver().resolveArtifact(releaseDescriptor);

        Optional.ofNullable(releasedArtifact).ifPresent(r ->
                release.describeProblem(b -> b.severity(Severity.PROBLEM)
                        .messageFormat("Found already released version {} for {}:{}")
                        .formatArgs(releaseVersion, currentDescriptor.getGroup(), currentDescriptor.getArtifact()))
        );

    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Checks if the artifact is not released yet.";
    }
}
