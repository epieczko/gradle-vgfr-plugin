package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import tane.mahuta.buildtools.apilyzer.clirr.ClirrApiCompatibilityReportBuilder
import tane.mahuta.buildtools.dependency.ArtifactResolver
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

    private final VcsExtension vcs
    private final VersioningExtension version
    private final ArtifactResolver artifactResolver

    private ReleaseInfrastructure releaseInfrastructure

    ReleaseExtension(@Nonnull final Project project) {
        this.vcs = project.extensions.findByType(VcsExtension)
        this.version = project.extensions.findByType(VersioningExtension)
        this.artifactResolver = new GradleArtifactResolver(project)
    }

    @Nonnull
    ReleaseInfrastructure getInfrastructure() {
        releaseInfrastructure = releaseInfrastructure ?: factorReleaseInfrastructure()
    }

    private ReleaseInfrastructure factorReleaseInfrastructure() {
        DefaultReleaseInfrastructure.builder().artifactResolver(artifactResolver)
                .versionHandler(FunctionalVersionHandler.builder().parser(version.parser).toReleaseVersionHandler(version.releaseTransformer).toReleaseVersionWithReportHandler(version.releaseTransformerForReport).comparator(version.comparator).build())
                .vcs(vcs)
                .apiCompatibilityReportBuilderFactory({ -> new ClirrApiCompatibilityReportBuilder() })
                .versionStorage(version.storage)
                .build()
    }

}
