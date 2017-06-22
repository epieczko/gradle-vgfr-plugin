package tane.mahuta.gradle.plugin.release.resolver

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedConfiguration
import tane.mahuta.buildtools.dependency.ArtifactResolver
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.ResolvedArtifactWithDependencies
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.buildtools.dependency.simple.DefaultResolvedArtifact
import tane.mahuta.buildtools.dependency.simple.DefaultResolvedArtifactWithDependencies

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@CompileStatic
class GradleArtifactResolver implements ArtifactResolver {

    private final Project project

    GradleArtifactResolver(@Nonnull final Project project) {
        this.project = project
    }

    @Override
    ResolvedArtifactWithDependencies resolveArtifact(@Nonnull final GAVCDescriptor descriptor) {
        final configuration = descriptor.with {
            project.configurations.detachedConfiguration(project.dependencies.create(group: group, name: artifact, version: version, classifier: classifier)).resolvedConfiguration
        }

        final artifact = configuration.resolvedArtifacts.find(this.&artifactMatchesDescriptor.curry(descriptor))

        final classpathDependencies = factorDependencies(configuration, descriptor)

        DefaultResolvedArtifactWithDependencies.builder()
                .descriptor(artifactDescriptor(artifact))
                .localFile(artifact.file)
                .classpathDependencies(classpathDependencies)
                .build()
    }

    @Override
    ResolvedArtifactWithDependencies resolveLastReleaseArtifact(@Nonnull final GAVCDescriptor currentDescriptor) {
        final StringBuilder sb = new StringBuilder(currentDescriptor.version)
        final m = currentDescriptor.version =~ /.\d/
        if (m.find()) {
            sb.delete(m.start(), sb.length())
        } else {
            sb.delete(0, sb.length())
        }
        sb.append("+")
        final descriptor = DefaultGAVCDescriptor.builder()
                .group(currentDescriptor.group)
                .artifact(currentDescriptor.artifact)
                .version(sb.toString())
                .classifier(currentDescriptor.classifier)
                .build()
        resolveArtifact(descriptor)
    }

    protected Set<tane.mahuta.buildtools.dependency.ResolvedArtifact> factorDependencies(ResolvedConfiguration configuration, GAVCDescriptor excludeDescriptor) {
        configuration.resolvedArtifacts.findAll {
            !artifactMatchesDescriptor(excludeDescriptor, it)
        }.collect {
            DefaultResolvedArtifact.builder()
                    .descriptor(artifactDescriptor(it))
                    .localFile(it.file).build() as tane.mahuta.buildtools.dependency.ResolvedArtifact
        } as Set
    }

    protected GAVCDescriptor artifactDescriptor(@Nonnull final ResolvedArtifact it) {
        def id = it.moduleVersion.id
        DefaultGAVCDescriptor.builder()
                .group(id.group)
                .artifact(id.name)
                .version(id.version)
                .classifier(it.classifier)
                .build()
    }

    protected boolean artifactMatchesDescriptor(@Nonnull final GAVCDescriptor descriptor,
                                                @Nonnull final org.gradle.api.artifacts.ResolvedArtifact artifact) {
        final id = artifact.moduleVersion.id
        id.group == descriptor.group && id.name == descriptor.artifact && artifact.classifier == descriptor.classifier
    }

}
