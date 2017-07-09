package tane.mahuta.gradle.plugin.release.resolver

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.Project
import tane.mahuta.buildtools.LogHelper
import tane.mahuta.buildtools.dependency.ArtifactResolver
import tane.mahuta.buildtools.dependency.ArtifactWithClasspath
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.gradle.plugin.release.GradleDomainObjectAdapter

import javax.annotation.Nonnull
import java.util.regex.Pattern

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@CompileStatic
@Slf4j
class GradleArtifactResolver implements ArtifactResolver {

    private final Project project

    private static final Pattern VERSION_EXTRACTION_PATTERN = Pattern.compile("^\\d+(\\.\\d+){1,2}")

    GradleArtifactResolver(@Nonnull final Project project) {
        this.project = project
    }

    @Override
    ArtifactWithClasspath resolveArtifact(@Nonnull final GAVCDescriptor descriptor) {
        project.configurations.maybeCreate('default')
        final configuration = descriptor.with {
            final mapNotation = [group: group, name: artifact, version: version]
            if (classifier) {
                mapNotation['classifier'] = classifier
            }
            final configuration = project.rootProject.configurations.detachedConfiguration(project.dependencies.create(mapNotation))
            configuration.extendsFrom = [] as Set
            configuration.resolvedConfiguration.lenientConfiguration
        }
        if (!configuration.unresolvedModuleDependencies.isEmpty()) {
            log.warn("Could not resolve the dependencies for artifact {}: \n{}", descriptor.toStringDescriptor(),
                    LogHelper.wrap({
                        configuration.unresolvedModuleDependencies.collect {
                            "${it.selector.group}:${it.selector.name}:${it.selector.version}: ${it.problem.message}" +
                                    (it.problem.cause ? it.problem.cause.message : "")
                        }.join(('\n'))
                    }))
            return null
        }
        GradleDomainObjectAdapter.instance.createArtifactWithClasspath(descriptor, configuration.artifacts)
    }

    @Override
    ArtifactWithClasspath resolveLastReleaseArtifact(@Nonnull final GAVCDescriptor currentDescriptor) {

        final matcher = VERSION_EXTRACTION_PATTERN.matcher(currentDescriptor.version)
        if (matcher.find()) {

            final origVersionParts = matcher.group(0).split('\\.')
            final versionParts = origVersionParts.collect(this.&safeToInt).findAll { it != null } as List<Integer>
            final from = versionParts.collect { 0 }.join('.')
            versionParts[versionParts.size() - 1] = Integer.MAX_VALUE
            final String to = versionParts.join('.')

            return resolveWithVersion(currentDescriptor, "[${from},${to}]")
        } else {
            return resolveWithVersion(currentDescriptor, "+")
        }
    }

    Integer safeToInt(String source) {
        try {
            source as Integer
        } catch (NumberFormatException e) {
            null
        }
    }

    protected ArtifactWithClasspath resolveWithVersion(
            final GAVCDescriptor currentDescriptor,
            final String version) {
        resolveArtifact(DefaultGAVCDescriptor.builder()
                .group(currentDescriptor.group)
                .artifact(currentDescriptor.artifact)
                .version(version)
                .classifier(currentDescriptor.classifier)
                .build())
    }

}
