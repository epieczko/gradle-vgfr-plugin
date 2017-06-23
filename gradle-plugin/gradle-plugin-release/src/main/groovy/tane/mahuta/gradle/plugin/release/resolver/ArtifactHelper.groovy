package tane.mahuta.gradle.plugin.release.resolver

import groovy.transform.CompileStatic
import org.apache.commons.lang.StringUtils
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedConfiguration
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.buildtools.dependency.simple.DefaultResolvedArtifact

import javax.annotation.Nonnull
/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@CompileStatic
class ArtifactHelper {

    private ArtifactHelper() {}

    static Set<tane.mahuta.buildtools.dependency.ResolvedArtifact> factorDependencies(ResolvedConfiguration configuration, GAVCDescriptor excludeDescriptor) {
        configuration.resolvedArtifacts.findAll {
            !artifactMatchesDescriptor(excludeDescriptor, it)
        }.collect {
            DefaultResolvedArtifact.builder()
                    .descriptor(artifactDescriptor(it))
                    .localFile(it.file).build() as tane.mahuta.buildtools.dependency.ResolvedArtifact
        } as Set
    }

    static GAVCDescriptor artifactDescriptor(@Nonnull final ResolvedArtifact it) {
        def id = it.moduleVersion.id
        DefaultGAVCDescriptor.builder()
                .group(id.group)
                .artifact(id.name)
                .version(id.version)
                .classifier(it.classifier)
                .build()
    }


    static boolean artifactMatchesDescriptor(@Nonnull final GAVCDescriptor descriptor,
                                             @Nonnull final ResolvedArtifact artifact) {
        final id = artifact.moduleVersion.id
        final classifier = StringUtils.isBlank(descriptor.classifier) ? null : descriptor.classifier
        id.group == descriptor.group && id.name == descriptor.artifact && artifact.classifier == classifier
    }
}
