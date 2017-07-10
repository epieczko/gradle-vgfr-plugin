package tane.mahuta.gradle.plugin

import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.version.VersionParser

/**
 * @author christian.heike@icloud.com
 * Created on 03.06.17.
 */
@Subject(VersionPlugin)
class VersionPluginTest extends Specification {

    @Rule
    @Delegate
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    def 'plugin application works, and version is default version'() {
        when:
        projectBuilder.project.apply plugin: VersionPlugin
        then:
        projectBuilder.project.version as String == 'unspecified'
    }

    def 'plugin creates version storage for gradle.properties'() {
        setup:
        projectBuilder.gradleProperties.version = '1.2.3'

        when:
        project.apply plugin: VersionPlugin
        then:
        project.versioning.storage != null
    }

    def 'plugin uses parser'() {
        setup:
        final parserMock = Mock(VersionParser)
        project.apply plugin: VersionPlugin

        when:
        project.versioning.parser = parserMock.&parse
        then:
        1 * parserMock.parse('unspecified', _) >> 'unspecified'
        when:
        project.version = "1.2.4"
        then:
        1 * parserMock.parse("1.2.4", _) >> "1.2.5"
        and:
        project.version == '1.2.5'
    }

}
