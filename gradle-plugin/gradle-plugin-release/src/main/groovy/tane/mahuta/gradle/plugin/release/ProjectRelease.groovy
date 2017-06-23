package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.PublishArtifact
import tane.mahuta.buildtools.dependency.DependencyContainer
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.ResolvedArtifact
import tane.mahuta.buildtools.dependency.simple.DefaultDependencyContainer
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.buildtools.release.AbstractArtifactRelease
import tane.mahuta.gradle.plugin.release.resolver.GradleArtifactResolver

import javax.annotation.Nonnull

/**
 * Gradle implementation of {@link AbstractArtifactRelease}.
 * 
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@CompileStatic
class ProjectRelease extends AbstractArtifactRelease {

    private final Project project
    private final PublishArtifact artifact

    private Set<ResolvedArtifact> classpathDependencies
    private Set<DependencyContainer> dependencyContainers

    ProjectRelease(@Nonnull final Project project,
                   @Nonnull final PublishArtifact artifact) {
        this.project = project
        this.artifact = artifact
    }

    @Override
    GAVCDescriptor getDescriptor() {
        DefaultGAVCDescriptor.builder()
                .group(project.group as String)
                .artifact(project.name)
                .version(project.version as String)
                .build()
    }

    @Override
    File getLocalFile() {
        artifact.file
    }

    @Override
    Set<ResolvedArtifact> getClasspathDependencies() {
        classpathDependencies = classpathDependencies ?: Collections.unmodifiableSet(resolvedArtifacts("compile"))
    }

    @Override
    Set<DependencyContainer> getDependencyContainers() {
        dependencyContainers = dependencyContainers ?: Collections.unmodifiableSet(factorDependencyContainers())
    }

    @Override
    File getProjectDir() {
        project.projectDir
    }

    private Set<DependencyContainer> factorDependencyContainers() {
        ['compile']
                .collect { project.configurations.findByName(it) }
                .findAll { it != null }
                .collect {
            DefaultDependencyContainer.builder()
                    .name(it.name)
                    .dependencies(resolvedArtifacts(it.name).collect { it.descriptor })
                    .build()
        } as Set

    }

    private Set<ResolvedArtifact> resolvedArtifacts(@Nonnull final String configName) {
        final config = project.configurations.findByName(configName)
        config ? GradleArtifactResolver.factorDependencies(config.resolvedConfiguration, getDescriptor()) : [] as Set
    }

}
