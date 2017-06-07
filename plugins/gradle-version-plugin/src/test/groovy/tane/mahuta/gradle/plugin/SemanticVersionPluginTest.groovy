package tane.mahuta.gradle.plugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.version.DefaultSemanticVersion
/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@Subject(SemanticVersionPlugin)
class SemanticVersionPluginTest extends Specification {

    private final Project project = ProjectBuilder.builder().build()

    def 'plugin loads version and transforms it to a semantic version'() {
        setup:
        new File(project.projectDir, "gradle.properties").text = 'version=1.2.3\n'

        when:
        project.apply plugin: SemanticVersionPlugin
        then:
        project.version == DefaultSemanticVersion.parse("1.2.3")
        and:
        project.version.toString() == "1.2.3"

        when:
        project.version = "1.2.4-SNAPSHOT"
        then:
        project.version.toString() == "1.2.4-SNAPSHOT"
    }

}
