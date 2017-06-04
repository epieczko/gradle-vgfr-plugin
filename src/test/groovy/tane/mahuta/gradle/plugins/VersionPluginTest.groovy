package tane.mahuta.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.gradle.plugins.version.VersionExtension

/**
 * @author christian.heike@icloud.com
 * Created on 03.06.17.
 */
@Subject(SemanticVersionPlugin)
class VersionPluginTest extends Specification {

    private final Project project = ProjectBuilder.builder().build()

    def 'plugin loads version and creates version storage'() {
        setup:
        new File(project.projectDir, "gradle.properties").text = 'version=1.2.3\n'

        when:
        project.apply plugin: VersionPlugin
        then:
        project.version instanceof VersionExtension
        and:
        project.version == "1.2.3"
        and:
        project.version.toString() == "1.2.3"

        when:
        project.version = "1.2.4"
        then:
        project.version instanceof VersionExtension
        and:
        project.version == "1.2.4"
        and:
        project.version.toString() == "1.2.4"
    }

}
