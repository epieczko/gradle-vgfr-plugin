package tane.mahuta.gradle.plugin

import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
/**
 * @author christian.heike@icloud.com
 * Created on 24.06.17.
 */
@Subject(ReleasePlugin)
class ReleasePluginTest extends Specification {

    @Rule
    @Delegate
    final ProjectBuilderTestRule rootBuilder = new ProjectBuilderTestRule()
    Project subProject

    def setup() {
        Git.init().setDirectory(root).call()
        final subDir = new File(root, "sub")
        subDir.mkdir()
        subProject = ProjectBuilder.builder().withName("sub").withProjectDir(subDir).withParent(project).build()
   }

    def 'application with root project applied throws exception'() {
        setup:
        project.apply plugin: ReleasePlugin
        when:
        subProject.apply plugin: ReleasePlugin
        then:
        thrown(PluginApplicationException)
    }

    def 'application with sub project applied throws exception'() {
        setup:
        project.apply plugin: ReleasePlugin
        when:
        subProject.apply plugin: ReleasePlugin
        then:
        thrown(PluginApplicationException)
    }

}
