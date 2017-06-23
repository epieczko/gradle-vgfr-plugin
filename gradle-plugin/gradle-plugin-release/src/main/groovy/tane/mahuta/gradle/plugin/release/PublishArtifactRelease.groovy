package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.gradle.api.Project
import org.gradle.api.artifacts.PublishArtifact
import tane.mahuta.buildtools.dependency.DependencyContainer
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.ResolvedArtifact
import tane.mahuta.buildtools.dependency.simple.DefaultDependencyContainer
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.buildtools.release.AbstractArtifactRelease
import tane.mahuta.gradle.plugin.release.resolver.ArtifactHelper

import javax.annotation.Nonnull

/**
 * Implementation of {@link AbstractArtifactRelease} for {@link PublishArtifact}s.
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@CompileStatic
@TupleConstructor
class PublishArtifactRelease extends AbstractArtifactRelease {

    @Nonnull
    GAVCDescriptor descriptor
    @Nonnull
    File localFile
    @Nonnull
    Set<ResolvedArtifact> classpathDependencies
    @Nonnull
    Set<DependencyContainer> dependencyContainers
    @Nonnull
    File projectDir

    static PublishArtifactRelease create(final Project project, final PublishArtifact artifact) {

        final descriptor = DefaultGAVCDescriptor.builder()
                .group(project.group as String)
                .artifact(project.name)
                .version(project.version as String)
                .classifier(artifact.classifier).build()

        final classpathDependencies = [] as Set

        final dependencyContainers = ['compile']
                .collect { project.configurations.findByName(it) }
                .findAll { it != null }
                .collect {
            final configDeps = ArtifactHelper.factorDependencies(it.resolvedConfiguration, descriptor)
            classpathDependencies.addAll(configDeps)

            DefaultDependencyContainer.builder().name(it.name)
                    .dependencies(configDeps.collect { it.descriptor })
                    .build()
        } as Set

        new PublishArtifactRelease(
                descriptor: descriptor,
                localFile: artifact.getFile(),
                classpathDependencies: classpathDependencies,
                dependencyContainers: dependencyContainers,
                projectDir: project.projectDir)
    }

}
