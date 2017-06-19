package tane.mahuta.gradle.plugin

import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.version.DefaultSemanticVersionParser

/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@Subject(SemanticVersionPlugin)
class SemanticVersionPluginTest extends Specification {

    @Rule
    @Delegate
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    def 'plugin loads version and transforms the version'() {
        setup:
        project.version = '1.2.3'
        final expectedVersion = DefaultSemanticVersionParser.instance.parse("1.2.3", null)
        final expectedSnapshotVersion = DefaultSemanticVersionParser.instance.parse("1.2.4-SNAPSHOT", null)

        when:
        project.apply plugin: SemanticVersionPlugin
        then:
        project.versioning.parser == DefaultSemanticVersionParser.instance
        and:
        project.version <=> expectedVersion == 0
        and:
        project.version == expectedVersion
        and:
        project.version as String == "1.2.3"

        when:
        project.version = "1.2.4-SNAPSHOT"
        then:
        project.version == expectedSnapshotVersion
    }

}
