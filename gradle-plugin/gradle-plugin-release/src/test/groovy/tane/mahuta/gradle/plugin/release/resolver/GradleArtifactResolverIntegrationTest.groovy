package tane.mahuta.gradle.plugin.release.resolver

import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.gradle.plugin.ProjectBuilderTestRule

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@Subject(GradleArtifactResolver)
class GradleArtifactResolverIntegrationTest extends Specification {

    @Rule
    @Delegate
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    private GradleArtifactResolver resolver

    def setup() {
        project.repositories {
            mavenCentral()
        }
        resolver = new GradleArtifactResolver(project)
    }

    @Unroll
    def 'latest release of #group:#artifact:#version = #expectedVersion (#expectedDependencies dependencies)'() {
        setup:
        final currentDescriptor = DefaultGAVCDescriptor.builder().group(group).artifact(artifact).version(version).build()
        final expectedDescriptor = DefaultGAVCDescriptor.builder().group(group).artifact(artifact).version(expectedVersion).build()

        when:
        final actualDescriptor = resolver.resolveLastReleaseArtifact(currentDescriptor)

        then:
        actualDescriptor.descriptor.group == expectedDescriptor.group
        actualDescriptor.descriptor.artifact == expectedDescriptor.artifact
        actualDescriptor.descriptor.version == expectedDescriptor.version
        actualDescriptor.descriptor.classifier == expectedDescriptor.classifier
        actualDescriptor.classpathDependencies.size() == expectedDependencies

        where:
        group              | artifact           | version                | expectedVersion        | expectedDependencies
        'commons-io'       | 'commons-io'       | '2.4'                  | '2.5'                  | 0
        'commons-io'       | 'commons-io'       | '1.8-SNAPSHOT'         | '1.4'                  | 0
        'junit'            | 'junit'            | '4.14-SNAPSHOT'        | '4.12'                 | 1
        'org.json'         | 'json'             | '20160810'             | '20170516'             | 0
        'org.eclipse.jgit' | 'org.eclipse.jgit' | '4.7.0.201704051617-r' | '4.7.1.201706071930-r' | 7

    }

}
