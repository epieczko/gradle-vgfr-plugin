package tane.mahuta.gradle.plugin

import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.version.DefaultSemanticVersion
import tane.mahuta.gradle.plugin.version.transform.ChangeLevel

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

    @Unroll
    def 'toNextSnapshot of #version with #changeLevel is #expected'() {
        setup:
        projectBuilder.gradleProperties.version = version
        projectBuilder.project.apply plugin: SemanticVersionPlugin

        expect:
        projectBuilder.project.version.toNextSnapshot(changeLevel) == DefaultSemanticVersion.parse(expected)

        where:
        version          | changeLevel                        | expected
        "1.2.3-SNAPSHOT" | ChangeLevel.API_INCOMPATIBILITY    | "2.2.3-SNAPSHOT"
        "1.2.3"          | ChangeLevel.API_INCOMPATIBILITY    | "2.2.3-SNAPSHOT"
        "1.2.3-SNAPSHOT" | ChangeLevel.API_EXTENSION          | "1.3.3-SNAPSHOT"
        "1.2.3"          | ChangeLevel.API_EXTENSION          | "1.3.3-SNAPSHOT"
        "1.2.3-SNAPSHOT" | null                               | "1.3.3-SNAPSHOT"
        "1.2.3"          | null                               | "1.3.3-SNAPSHOT"
        "1.2-SNAPSHOT"   | ChangeLevel.API_EXTENSION          | "1.3-SNAPSHOT"
        "1.2"            | ChangeLevel.API_EXTENSION          | "1.3-SNAPSHOT"
        "1.2-SNAPSHOT"   | null                               | "1.3-SNAPSHOT"
        "1.2"            | null                               | "1.3-SNAPSHOT"
        "1.2.3-SNAPSHOT" | ChangeLevel.IMPLEMENTATION_CHANGED | "1.2.4-SNAPSHOT"
        "1.2.3"          | ChangeLevel.IMPLEMENTATION_CHANGED | "1.2.4-SNAPSHOT"
        "1.2-SNAPSHOT"   | ChangeLevel.IMPLEMENTATION_CHANGED | "1.2-SNAPSHOT"
        "1.2"            | ChangeLevel.IMPLEMENTATION_CHANGED | "1.2-SNAPSHOT"
    }

    @Unroll
    def 'toRelease of #version is #expected'() {
        setup:
        projectBuilder.gradleProperties.version = version
        projectBuilder.project.apply plugin: SemanticVersionPlugin

        expect:
        projectBuilder.project.version.toRelease() == DefaultSemanticVersion.parse(expected)

        where:
        version          | expected
        "1.2-SNAPSHOT"   | "1.2"
        "1.2"            | "1.2"
        "1.2.3-SNAPSHOT" | "1.2.3"
        "1.2.3"          | "1.2.3"
    }

}
