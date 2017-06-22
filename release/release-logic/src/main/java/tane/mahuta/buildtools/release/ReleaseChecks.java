package tane.mahuta.buildtools.release;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;
import tane.mahuta.buildtools.apilyzer.Scope;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;
import tane.mahuta.buildtools.dependency.ResolvedArtifact;
import tane.mahuta.buildtools.dependency.ResolvedArtifactWithDependencies;
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor;
import tane.mahuta.buildtools.release.reporting.Severity;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Default release checks based on a {@link ArtifactRelease} using {@link ReleaseInfrastructure}.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
@RequiredArgsConstructor
public class ReleaseChecks<V extends Comparable<? super V>> {

    private static final Logger log = LoggerFactory.getLogger(ReleaseChecks.class);
    private static final List<Function<VcsFlowConfig, String>> DEFAULT_RELEASABLE_BRANCHES = Arrays.asList(
            VcsFlowConfig::getDevelopmentBranch,
            VcsFlowConfig::getHotfixBranchPrefix,
            VcsFlowConfig::getSupportBranchPrefix,
            VcsFlowConfig::getReleaseBranchPrefix
    );

    /**
     * The project release to run.
     */
    @NonNull
    private final ArtifactRelease project;

    /**
     * The service to use.
     */
    @Delegate
    @NonNull
    private final ReleaseInfrastructure<V> releaseInfrastructure;

    /**
     * Checks if the there are uncommitted changes and reports it to the project.
     *
     * @return {@code this}
     */
    public ReleaseChecks<V> noUncommittedChanges() {
        final Collection<String> uncommited = getVcs().getUncommittedFilePaths();
        if (!uncommited.isEmpty()) {
            project.describeProblem(b -> b.severity(Severity.PROBLEM)
                    .messageFormat("Found uncommitted changes: {}")
                    .formatArgs(uncommited));
        }
        return this;
    }

    /**
     * Checks if the project has already been released.
     *
     * @return {@code this}
     */
    public ReleaseChecks<V> projectNotAlreadyReleased() {

        final String releaseVersion = getReleaseVersion().toString();

        final GAVCDescriptor releaseDescriptor = DefaultGAVCDescriptor.builder()
                .group(project.getDescriptor().getGroup())
                .artifact(project.getDescriptor().getArtifact())
                .version(releaseVersion).build();

        final ResolvedArtifact releasedArtifact = getArtifactResolver().resolveArtifact(releaseDescriptor);

        Optional.ofNullable(releasedArtifact).ifPresent(r ->
                project.describeProblem(b -> b.severity(Severity.PROBLEM)
                        .messageFormat("Found already released version {} for {}:{}")
                        .formatArgs(releaseVersion, project.getDescriptor().getGroup(), project.getDescriptor().getArtifact()))
        );

        return this;
    }

    /**
     * Checks if the project contains any snapshot dependencies.
     *
     * @return {@code this}
     */
    public ReleaseChecks<V> projectReferencesNoSnapshotDependencies() {

        project.getDependencyContainers().forEach(container -> {

            final List<String> snapshotDependencies = StreamSupport.stream(container.spliterator(), false)
                    .filter(GAVCDescriptor::isSnapshot)
                    .map(GAVCDescriptor::toStringDescriptor)
                    .collect(Collectors.toList());

            if (!snapshotDependencies.isEmpty()) {
                project.describeProblem(b -> b.severity(Severity.PROBLEM)
                        .messageFormat("Dependency container {} contains snapshot dependencies: {}")
                        .formatArgs(container.getName(), snapshotDependencies));
            }

        });
        return this;
    }

    /**
     * Checks if the version to be released matches the API compatibility report.
     *
     * @return {@code this}
     */
    public ReleaseChecks<V> releaseVersionMatchesAtLeastApiCompatibility() {

        if (project.getLocalFile() == null) {
            log.debug("releaseVersionMatchesAtLeastApiCompatibility: Release does not provide an artifact, skipping check.");
        }

        final V currentReleaseVersion = getReleaseVersion();
        final GAVCDescriptor releaseDescriptor = DefaultGAVCDescriptor.builder()
                .group(project.getDescriptor().getGroup())
                .artifact(project.getDescriptor().getArtifact())
                .version(currentReleaseVersion.toString()).build();

        final ResolvedArtifactWithDependencies lastRelease = getArtifactResolver().resolveLastReleaseArtifact(releaseDescriptor);

        Optional.ofNullable(lastRelease)
                .map(this::createApiCompatibilityReport)
                .map(this::mapToReleaseVersion)
                .ifPresent(necessaryReleaseVersion -> {
                    if (necessaryReleaseVersion.compareTo(currentReleaseVersion) > 0) {
                        project.describeProblem(b -> b.severity(Severity.PROBLEM)
                                .messageFormat("The version to be released ({}) is smaller than the determined release version for API changes: {}")
                                .formatArgs(currentReleaseVersion, necessaryReleaseVersion));
                    }
                });

        return this;
    }

    /**
     * Check if the VCS points to a releasable branch.
     *
     * @return {@code this}
     */
    public ReleaseChecks<V> releasableBranch() {
        final String branch = getVcs().getBranch();
        if (branch != null) {
            if (!getSupportedReleaseBranchNames().anyMatch(getVcs()::isOnBranch)) {
                project.describeProblem(b -> b.severity(Severity.PROBLEM)
                        .messageFormat("Not on a releasable branch: {}")
                        .formatArgs(branch));
            }
        }
        return this;
    }

    /**
     * @return a stream of supported branch names or prefixes
     */
    @Nonnull
    protected Stream<String> getSupportedReleaseBranchNames() {
        final VcsFlowConfig flowConfig = getVcs().getFlowConfig();
        return DEFAULT_RELEASABLE_BRANCHES.stream().map(f -> f.apply(flowConfig));
    }

    @Nonnull
    private V mapToReleaseVersion(@Nonnull final ApiCompatibilityReport apiCompatibilityReport) {
        final V version = getParsedVersion();
        return getVersionHandler().toReleaseVersionWithReport(version, apiCompatibilityReport);
    }

    private V getParsedVersion() {
        return getVersionHandler().parse(project.getDescriptor().getVersion(), project.getProjectDir());
    }

    @Nullable
    private ApiCompatibilityReport createApiCompatibilityReport(@Nonnull final ResolvedArtifactWithDependencies baseLineArtifact) {

        final Set<File> baseLineClasspath = collectFiles(baseLineArtifact.getClasspathDependencies());
        final Set<File> currentClasspath = collectFiles(project.getClasspathDependencies());

        return getApiCompatibilityReportBuilderFactory().builder()
                .withCurrent(project.getLocalFile())
                .withCurrentClasspath(currentClasspath)
                .withBaseline(baseLineArtifact.getLocalFile())
                .withBaselineClasspath(baseLineClasspath)
                .withLogger(log)
                .withScope(Scope.PUBLIC)
                .buildReport();
    }

    @Nonnull
    private static Set<File> collectFiles(@Nonnull final Set<ResolvedArtifact> artifacts) {
        return artifacts.stream().map(ResolvedArtifact::getLocalFile).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * @return the release version for the project
     */
    private V getReleaseVersion() {
        return getVersionHandler().toReleaseVersion(getParsedVersion());
    }

}
