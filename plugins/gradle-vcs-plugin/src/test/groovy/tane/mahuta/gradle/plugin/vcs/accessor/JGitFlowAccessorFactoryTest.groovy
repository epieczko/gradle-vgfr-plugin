package tane.mahuta.gradle.plugin.vcs.accessor

import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject
/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
@Subject(JGitFlowAccessorFactory)
class JGitFlowAccessorFactoryTest extends Specification {

    private final Project project = ProjectBuilder.builder().build()

    private final JGitFlowAccessorFactory factory = new JGitFlowAccessorFactory()

    def 'creates accessor for git based project'() {
        expect:
        factory.create(project) == null

        when:
        Git.init().setDirectory(project.projectDir).call()
        then:
        factory.create(project) != null
    }


}
