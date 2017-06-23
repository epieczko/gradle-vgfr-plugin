package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.PublishArtifact
import tane.mahuta.buildtools.apilyzer.clirr.ClirrApiCompatibilityReportBuilder
import tane.mahuta.buildtools.dependency.ArtifactResolver
import tane.mahuta.buildtools.release.ArtifactRelease
import tane.mahuta.buildtools.release.DefaultReleaseInfrastructure
import tane.mahuta.buildtools.release.FunctionalVersionHandler
import tane.mahuta.buildtools.release.ReleaseInfrastructure
import tane.mahuta.gradle.plugin.release.resolver.GradleArtifactResolver
import tane.mahuta.gradle.plugin.vcs.VcsExtension
import tane.mahuta.gradle.plugin.version.VersioningExtension

import javax.annotation.Nonnull

/**
 * Extension for the {@link tane.mahuta.gradle.plugin.ReleasePlugin}.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
class ReleaseExtension {


    private final Project project
    private final ArtifactResolver artifactResolver

    private ReleaseInfrastructure releaseInfrastructure
    private Set<ArtifactRelease> artifactReleases

    ReleaseExtension(@Nonnull final Project project) {
        this.project = project
        this.artifactResolver = new GradleArtifactResolver(project)
    }

    protected VersioningExtension getVersionExtension() {
        project.extensions.findByType(VersioningExtension)
    }

    protected VcsExtension getVcs() {
        project.extensions.findByType(VcsExtension)
    }

    @Nonnull
    ReleaseInfrastructure getInfrastructure() {
        releaseInfrastructure = releaseInfrastructure ?: factorReleaseInfrastructure()
    }

    @Nonnull
    Set<ArtifactRelease> getArtifactReleases() {
        artifactReleases = artifactReleases ?: factorArtifactReleases()
    }

    private ReleaseInfrastructure factorReleaseInfrastructure() {
        DefaultReleaseInfrastructure.builder().artifactResolver(artifactResolver)
                .versionHandler(factorVersionHandler())
                .vcs(vcs)
                .apiCompatibilityReportBuilderFactory({ -> new ClirrApiCompatibilityReportBuilder() })
                .versionStorage(versionExtension.storage)
                .build()
    }

    @Nonnull
    private FunctionalVersionHandler factorVersionHandler() {
        FunctionalVersionHandler.builder()
                .parser(versionExtension.parser)
                .toReleaseVersionHandler(versionExtension.releaseTransformer)
                .toReleaseVersionWithReportHandler(versionExtension.releaseTransformerForReport)
                .comparator(versionExtension.comparator).build()
    }

    private Set<ArtifactRelease> factorArtifactReleases() {
        project.configurations.collect { it.artifacts }.collect { artifacts ->
            artifacts.collect { PublishArtifactRelease.create(project, it as PublishArtifact) } as Set<ArtifactRelease>
        }.flatten() as Set<ArtifactRelease>
    }

}
