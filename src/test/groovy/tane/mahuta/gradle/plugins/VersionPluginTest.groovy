package tane.mahuta.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
/**
 * @author christian.heike@icloud.com
 * Created on 03.06.17.
 */
class VersionPluginTest extends Specification {

    private final Project project = ProjectBuilder.builder().build()

    def 'plugin loads version and creates version storage'() {
        setup:
        new File(project.projectDir, "gradle.properties").text = 'version=1.2.3\n'

        when:
        project.apply plugin: VersionPlugin
        then:
        project.extensions.findByName("versionStorage") != null
        and:
        project.version == "1.2.3"
    }

}
