package tane.mahuta.gradle.plugin

import org.eclipse.jgit.api.Git
import org.gradle.api.internal.plugins.PluginApplicationException
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
@Subject(VcsPlugin)
class VcsPluginTest extends Specification {

    @Rule
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    def 'throws exception if no VCS is available'() {
        when:
        projectBuilder.project.apply plugin: VcsPlugin
        then:
        thrown(PluginApplicationException)
    }

    def 'creates vcs extension'() {
        setup:
        Git.init().setDirectory(projectBuilder.root).call()

        when:
        projectBuilder.project.apply plugin: VcsPlugin
        then:
        projectBuilder.project.vcs.branch == "master"
    }

}
