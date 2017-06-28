package tane.mahuta.gradle.plugin.release

import org.eclipse.jgit.api.Git
import org.junit.Rule
import spock.lang.Requires
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.gradle.plugin.OnlineCheck
import tane.mahuta.gradle.plugin.ProjectBuilderTestRule
import tane.mahuta.gradle.plugin.SemanticVersionPlugin
import tane.mahuta.gradle.plugin.VcsPlugin
/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@Subject(ReleaseExtension)
class ReleaseExtensionIntegrationTest extends Specification {

    @Delegate
    @Rule
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    private ReleaseExtension extension

    def setup() {
        Git.init().setDirectory(project.projectDir).call()
        project.version = '1.2.3'
        gradleProperties.version = '1.2.3'
        project.apply plugin: VcsPlugin
        project.apply plugin: SemanticVersionPlugin
        extension = project.extensions.create('release', ReleaseExtension, project)
    }

    @Requires({ OnlineCheck.online })
    def 'creates infrastructure correctly'() {
        when:
        final infrastructure = extension.infrastructure
        then:
        infrastructure != null
        expect: 'factor the instance just once'
        infrastructure.is(extension.infrastructure)
    }

    def 'creates artifact releases'() {
        project.apply plugin: 'java'
        new File(project.projectDir, "src/main/java/A.java").with {
            parentFile.mkdirs()
            delegate << "class A { }\n"

        }
        project.repositories {
            mavenCentral()
        }
        project.dependencies {
            compile 'commons-io:commons-io:2.4'
        }

        when:
        final releases = extension.artifactReleases
        then:
        releases.size() == 1

        when:
        final release = releases[0]
        final descriptor = release.descriptor
        then:
        release.localFile != null
        release.dependencyContainers.size() == 1

        descriptor.group == project.group as String
        descriptor.artifact == project.name as String
        descriptor.version == project.version as String
        descriptor.classifier == null
        and:
        release.classpathDependencies.size() == 1

        when:
        final dependency = release.classpathDependencies[0]
        final container = release.dependencyContainers[0]
        final dependencyDescriptor = dependency.descriptor

        then:
        dependency.localFile != null
        dependencyDescriptor.group == 'commons-io'
        dependencyDescriptor.artifact == 'commons-io'
        dependencyDescriptor.version == '2.4'
        dependencyDescriptor.classifier == null
        container.name == 'compile'
        container.dependencies.size() == 1

        expect:
        releases.is(extension.artifactReleases)
    }

}
