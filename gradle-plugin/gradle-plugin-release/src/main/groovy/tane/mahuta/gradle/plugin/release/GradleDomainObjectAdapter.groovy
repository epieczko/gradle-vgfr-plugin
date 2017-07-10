package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.artifacts.ResolvedArtifact
import tane.mahuta.buildtools.dependency.Artifact
import tane.mahuta.buildtools.dependency.ArtifactWithClasspath
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.simple.DefaultArtifact
import tane.mahuta.buildtools.dependency.simple.DefaultArtifactWithClasspath
import tane.mahuta.buildtools.dependency.simple.DefaultDependencyContainer
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.buildtools.release.ArtifactRelease
import tane.mahuta.buildtools.release.DefaultArtifactRelease

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Helper for converting from the gradle domain to the release domain.
 *
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@CompileStatic
@Singleton
class GradleDomainObjectAdapter {

    /**
     * Factors the {@link Artifact} for a {@link ResolvedArtifact}.
     * @param resolveArtifact the resolved artifact
     * @param projectDependencies the internal dependency declarations
     * @return the artifact
     */
    @Nonnull
    Artifact createArtifact(@Nonnull final ResolvedArtifact resolveArtifact,
                            @Nonnull final Set<ProjectDependency> projectDependencies = [] as Set) {
        final descriptor = createGAVDescriptor(resolveArtifact)
        final internal = projectDependencies.any {
            it.name == descriptor.artifact &&
                    it.group == descriptor.group &&
                    it.version == descriptor.version
        }
        DefaultArtifact.builder()
                .descriptor(descriptor)
                .localFile(resolveArtifact.file)
                .internalArtifact(internal)
                .build()
    }

    /**
     * Factors the {@link GAVCDescriptor} for a {@link ResolvedArtifact}.
     * @param resolvedArtifact the artifact
     * @return the descriptor
     */
    @Nonnull
    GAVCDescriptor createGAVDescriptor(@Nonnull final ResolvedArtifact resolvedArtifact) {
        final id = resolvedArtifact.moduleVersion.id
        DefaultGAVCDescriptor.builder().group(id.group).artifact(id.name).version(id.version).classifier(resolvedArtifact.classifier).build()
    }

    /**
     * Factors the {@link GAVCDescriptor} for a {@link Project} and a {@link PublishArtifact}.
     * @param project the project
     * @param artifact the artifact
     * @return the descriptor
     */
    @Nonnull
    GAVCDescriptor createGAVDescriptor(@Nonnull final Project project, @Nonnull final PublishArtifact artifact) {
        DefaultGAVCDescriptor.builder().group(project.group as String).artifact(project.name).version(project.version as String).classifier(artifact.classifier).build()
    }

    /**
     * Factors the {@link ArtifactWithClasspath} for a provided {@link GAVCDescriptor} and {@link ResolvedArtifact}s containing the resolved dependencies.
     * @param descriptor the descriptor
     * @param resolvedArtifacts the resolved dependencies
     * @return the artifact with the class path
     */
    @Nonnull
    ArtifactWithClasspath createArtifactWithClasspath(
            @Nonnull final GAVCDescriptor descriptor, @Nonnull final Set<ResolvedArtifact> resolvedArtifacts) {
        final matchingArtifact = resolvedArtifacts.find(this.&matchesDescriptor.curry(descriptor))
        createArtifactWithClasspath(descriptor, (matchingArtifact as ResolvedArtifact)?.file, resolvedArtifacts)
    }

    /**
     /**
     * Factors the {@link ArtifactWithClasspath} for a provided {@link GAVCDescriptor}, {@link File} and {@link ResolvedArtifact}s containing the resolved dependencies.
     * @param descriptor the descriptor
     * @param localFile the optional local file for the artifact itself
     * @param resolvedArtifacts the resolved dependencies
     * @return the artifact with the class path
     */
    @Nonnull
    ArtifactWithClasspath createArtifactWithClasspath(
            @Nonnull final GAVCDescriptor descriptor,
            @Nullable final File localFile,
            @Nonnull final Set<ResolvedArtifact> resolvedArtifacts) {

        final matchingArtifact = resolvedArtifacts.find(this.&matchesDescriptor.curry(descriptor))

        final classpathDependencies = resolvedArtifacts.findAll {
            it != matchingArtifact
        }.collect(this.&createArtifact) as Set

        DefaultArtifactWithClasspath.builder()
                .descriptor(createGAVDescriptor(matchingArtifact))
                .localFile(localFile)
                .classpathDependencies(classpathDependencies)
                .build()
    }

    /**
     * Factors an {@link ArtifactRelease} for a {@link PublishArtifact} in a {@link Project}.
     * @param project the project
     * @param artifact the artifact in the project
     * @return the release
     */
    @Nonnull
    ArtifactRelease createArtifactRelease(@Nonnull final Project project,
                                          @Nonnull final PublishArtifact artifact) {

        final classpathDependencies = [] as Set

        final dependencyContainers = findConfigurations(project, ['compile']).collect {

            final Set<ProjectDependency> projectDependencies = it.dependencies.findAll {
                it instanceof ProjectDependency
            }.collect { it as ProjectDependency } as Set
            final Collection<Artifact> dependencies = it.resolvedConfiguration.resolvedArtifacts
                    .collect(GradleDomainObjectAdapter.instance.&createArtifact.ncurry(1, projectDependencies))

            classpathDependencies.addAll(dependencies)
            DefaultDependencyContainer.builder()
                    .name(it.name)
                    .externalDependencies(dependencies.findAll { !it.internalArtifact }.collect {
                it.descriptor
            } as Set)
                    .internalDependencies(dependencies.findAll { it.internalArtifact }.collect { it.descriptor } as Set)
                    .build()
        } as Set

        DefaultArtifactRelease.builder()
                .descriptor(createGAVDescriptor(project, artifact))
                .localFile(artifact.getFile())
                .classpathDependencies(classpathDependencies)
                .dependencyContainers(dependencyContainers)
                .projectDir(project.projectDir).build()
    }

    private Collection<Configuration> findConfigurations(
            @Nonnull final Project project, @Nonnull final Iterable<String> names) {
        names.collect { project.configurations.findByName(it as String) }.findAll { it != null }
    }

    private boolean matchesDescriptor(
            @Nonnull final GAVCDescriptor descriptor, @Nonnull final ResolvedArtifact resolvedArtifact) {
        final resolvedDescriptor = createGAVDescriptor(resolvedArtifact)
        resolvedDescriptor.group == descriptor.group &&
                resolvedDescriptor.artifact == descriptor.artifact &&
                resolvedDescriptor.classifier == descriptor.classifier
    }
}