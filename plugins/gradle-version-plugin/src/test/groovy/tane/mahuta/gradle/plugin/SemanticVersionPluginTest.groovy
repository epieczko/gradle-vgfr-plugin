package tane.mahuta.gradle.plugin

import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.version.DefaultSemanticVersion
/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@Subject(SemanticVersionPlugin)
class SemanticVersionPluginTest extends Specification {

    @Rule
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    def 'plugin loads version and transforms it to a semantic version'() {
        setup:
        projectBuilder.gradleProperties.version = "1.2.3"

        when:
        projectBuilder.project.apply plugin: SemanticVersionPlugin
        then:
        projectBuilder.project.version == DefaultSemanticVersion.parse("1.2.3")
        and:
        projectBuilder.project.version as String == "1.2.3"

        when:
        projectBuilder.project.version = "1.2.4-SNAPSHOT"
        then:
        projectBuilder.project.version.toString() == "1.2.4-SNAPSHOT"
    }

}
