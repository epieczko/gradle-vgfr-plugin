package tane.mahuta.buildtools.release.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;
import tane.mahuta.buildtools.apilyzer.Scope;
import tane.mahuta.buildtools.dependency.Artifact;
import tane.mahuta.buildtools.dependency.ArtifactWithClasspath;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor;
import tane.mahuta.buildtools.release.AbstractGuardedReleaseStep;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;
import tane.mahuta.buildtools.release.reporting.Severity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Checks if the release version of the artifact matches the one when including the api compatibility.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.06.17.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ReleaseVersionMatchesApiCompatibilityCheck extends AbstractGuardedReleaseStep {

    private static final class InstanceHolder {
        private static final ReleaseVersionMatchesApiCompatibilityCheck INSTANCE = new ReleaseVersionMatchesApiCompatibilityCheck();
    }

    public static ReleaseVersionMatchesApiCompatibilityCheck getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected void doApply(@Nonnull final ArtifactRelease release,
                        @Nonnull final ReleaseInfrastructure releaseInfrastructure,
                        @Nonnull final Object version) {

        if (release.getLocalFile() == null) {
            log.debug("releaseVersionMatchesAtLeastApiCompatibility: Release does not provide an artifact, skipping check.");
            return;
        }

        final GAVCDescriptor currentDescriptor = release.getDescriptor();
        final Object parsedCurrentVersion = releaseInfrastructure.getVersionHandler().parse(currentDescriptor.getVersion(), release.getProjectDir());
        final Object parsedCurrentReleaseVersion = releaseInfrastructure.getVersionHandler().toReleaseVersion(parsedCurrentVersion);

        final GAVCDescriptor releaseDescriptor = DefaultGAVCDescriptor.builder()
                .group(currentDescriptor.getGroup())
                .artifact(currentDescriptor.getArtifact())
                .version(parsedCurrentReleaseVersion.toString()).build();

        final ArtifactWithClasspath lastRelease = releaseInfrastructure.getArtifactResolver().resolveLastReleaseArtifact(releaseDescriptor);
        if (lastRelease == null) {
            log.warn("Could not find latest release for: {}", releaseDescriptor);
            return; // No last release
        }
        final Object parsedLastReleaseVersion = releaseInfrastructure.getVersionHandler().parse(lastRelease.getDescriptor().getVersion(), release.getProjectDir());

        Optional.ofNullable(lastRelease)
                .map(r -> createApiCompatibilityReport(r, release, releaseInfrastructure))
                .map(report -> releaseInfrastructure.getVersionHandler().toNextReleaseVersion(parsedLastReleaseVersion, report))
                .ifPresent(necessaryReleaseVersion -> {
                    if (releaseInfrastructure.getVersionHandler().getComparator().compare(necessaryReleaseVersion, parsedCurrentReleaseVersion) > 0) {
                        release.describeProblem(b -> b.severity(Severity.PROBLEM)
                                .messageFormat("The version to be released ({}) is smaller than the determined release version for API changes: {}")
                                .formatArgs(parsedCurrentReleaseVersion, necessaryReleaseVersion));
                    }
                });
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Checks if the release version matches the API compatibility";
    }

    @Nullable
    protected ApiCompatibilityReport createApiCompatibilityReport(@Nonnull final ArtifactWithClasspath baseLineArtifact,
                                                                  @Nonnull final ArtifactRelease release,
                                                                  @Nonnull final ReleaseInfrastructure releaseInfrastructure) {

        final Set<File> baseLineClasspath = collectFiles(baseLineArtifact.getClasspathDependencies());
        final Set<File> currentClasspath = collectFiles(release.getClasspathDependencies());

        return releaseInfrastructure.getApiCompatibilityReportBuilderFactory().builder()
                .withCurrent(release.getLocalFile())
                .withCurrentClasspath(currentClasspath)
                .withBaseline(baseLineArtifact.getLocalFile())
                .withBaselineClasspath(baseLineClasspath)
                .withLogger(log)
                .withScope(Scope.PUBLIC)
                .buildReport();
    }

    @Nonnull
    protected static Set<File> collectFiles(@Nonnull final Set<? extends Artifact> artifacts) {
        return artifacts.stream().map(Artifact::getLocalFile).filter(Objects::nonNull).collect(Collectors.toSet());
    }

}
