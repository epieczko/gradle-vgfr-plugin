package tane.mahuta.buildtools.vcs.jgit

import org.eclipse.jgit.api.Git
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.vcs.VcsAccessorFactory

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
@Subject(JGitFlowAccessorFactory)
class JGitFlowAccessorFactoryTest extends Specification {

    @Rule
    final TemporaryFolder temporaryFolder = new TemporaryFolder()

    private final JGitFlowAccessorFactory factory = new JGitFlowAccessorFactory()

    def 'creates accessor for git based directories'() {
        setup:
        final subdir = new File(temporaryFolder.root, "a/b/c")
        subdir.mkdirs()

        expect:
        factory.create(temporaryFolder.root) == null

        when:
        Git.init().setDirectory(temporaryFolder.root).call()
        and:
        final factored = factory.create(subdir)
        then:
        factored != null
        and:
        factory.create(subdir.parentFile).is(factored)
    }

    def 'service loader definition is found'() {
        ServiceLoader.load(VcsAccessorFactory).iterator().collect{ it.class }.contains(JGitFlowAccessorFactory)
    }

}
