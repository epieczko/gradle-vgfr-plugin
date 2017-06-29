package tane.mahuta.gradle.plugin.release.resolver

import groovy.transform.CompileStatic
import org.gradle.api.Project
import tane.mahuta.buildtools.dependency.ArtifactResolver
import tane.mahuta.buildtools.dependency.ArtifactWithClasspath
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.gradle.plugin.release.GradleDomainObjectAdapter

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
    ArtifactWithClasspath resolveArtifact(@Nonnull final GAVCDescriptor descriptor) {
        final configuration = descriptor.with {
            project.configurations.detachedConfiguration(project.dependencies.create(group: group, name: artifact, version: version, classifier: classifier)).resolvedConfiguration
        }
        GradleDomainObjectAdapter.instance.createArtifactWithClasspath(descriptor, configuration.resolvedArtifacts)
    }

    @Override
    ArtifactWithClasspath resolveLastReleaseArtifact(@Nonnull final GAVCDescriptor currentDescriptor) {
        final StringBuilder sb = new StringBuilder(currentDescriptor.version)
        removeLastVersionPart(sb)
        sb.append("+")
        final descriptor = DefaultGAVCDescriptor.builder()
                .group(currentDescriptor.group)
                .artifact(currentDescriptor.artifact)
                .version(sb.toString())
                .classifier(currentDescriptor.classifier)
                .build()
        resolveArtifact(descriptor)
    }

    protected void removeLastVersionPart(final StringBuilder sb) {
        // Match the default semantic version scheme
        final m = sb.toString() =~ /^\d+(\.\d+){0,2}/
        if (m.find()) {
            // If found, remove the rest of the version string
            sb.delete(m.start(), sb.length())
            final matched = m.group(0)
            if (matched.contains('.')) {
                sb.insert(m.start(), matched.substring(0, matched.lastIndexOf('.')))
            }
        } else {
            sb.delete(0, sb.length())
        }
    }

}
