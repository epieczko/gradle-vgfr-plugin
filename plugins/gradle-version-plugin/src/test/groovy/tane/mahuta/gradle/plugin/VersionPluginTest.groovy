package tane.mahuta.gradle.plugin

import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.gradle.plugin.version.VersionExtension

/**
 * @author christian.heike@icloud.com
 * Created on 03.06.17.
 */
@Subject(VersionPlugin)
class VersionPluginTest extends Specification {

    @Rule
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    def 'plugin without version storage returns default version'() {
        when:
        projectBuilder.project.apply plugin: VersionPlugin
        then:
        projectBuilder.project.version as String == 'unspecified'
    }

    def 'plugin loads version and creates version storage'() {
        setup:
        projectBuilder.gradleProperties.version = '1.2.3'

        when:
        projectBuilder.project.apply plugin: VersionPlugin
        then:
        projectBuilder.project.version instanceof VersionExtension
        and:
        projectBuilder.project.version == "1.2.3"
        and:
        projectBuilder.project.version.toString() == "1.2.3"

        when:
        projectBuilder.project.version = "1.2.4"
        then:
        projectBuilder.project.version instanceof VersionExtension
        and:
        projectBuilder.project.version == "1.2.4"
        and:
        projectBuilder.project.version.toString() == "1.2.4"
    }

}
