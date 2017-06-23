package tane.mahuta.buildtools.release.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;
import tane.mahuta.buildtools.apilyzer.Scope;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;
import tane.mahuta.buildtools.dependency.ResolvedArtifact;
import tane.mahuta.buildtools.dependency.ResolvedArtifactWithDependencies;
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;
import tane.mahuta.buildtools.release.ReleaseStep;
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
public class ReleaseVersionMatchesApiCompatibilityCheck implements ReleaseStep {

    private static final class InstanceHolder {
        private static final ReleaseVersionMatchesApiCompatibilityCheck INSTANCE = new ReleaseVersionMatchesApiCompatibilityCheck();
    }

    public static ReleaseVersionMatchesApiCompatibilityCheck getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void apply(@Nonnull ArtifactRelease release, @Nonnull ReleaseInfrastructure releaseInfrastructure) {

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

        final ResolvedArtifactWithDependencies lastRelease = releaseInfrastructure.getArtifactResolver().resolveLastReleaseArtifact(releaseDescriptor);

        Optional.ofNullable(lastRelease)
                .map(r -> createApiCompatibilityReport(r, release, releaseInfrastructure))
                .map(report -> releaseInfrastructure.getVersionHandler().toReleaseVersionWithReport(parsedCurrentVersion, report))
                .ifPresent(necessaryReleaseVersion -> {
                    if (releaseInfrastructure.getVersionHandler().getComparator().compare(necessaryReleaseVersion, parsedCurrentReleaseVersion) > 0) {
                        release.describeProblem(b -> b.severity(Severity.PROBLEM)
                                .messageFormat("The version to be released ({}) is smaller than the determined release version for API changes: {}")
                                .formatArgs(parsedCurrentReleaseVersion, necessaryReleaseVersion));
                    }
                });
    }

    @Nullable
    protected ApiCompatibilityReport createApiCompatibilityReport(@Nonnull final ResolvedArtifactWithDependencies baseLineArtifact,
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
    protected static Set<File> collectFiles(@Nonnull final Set<ResolvedArtifact> artifacts) {
        return artifacts.stream().map(ResolvedArtifact::getLocalFile).filter(Objects::nonNull).collect(Collectors.toSet());
    }

}
