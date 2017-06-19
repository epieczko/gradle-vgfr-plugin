package tane.mahuta.buildtools.vcs.jgit

import com.atlassian.jgitflow.core.InitContext
import com.atlassian.jgitflow.core.JGitFlow
import com.atlassian.jgitflow.core.JGitFlowWithConfig
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
@Subject(JGitFlowAccessor)
class JGitFlowAccessorTest extends Specification {

    private static final InitContext INIT_CTX = new InitContext()
            .setDevelop("develop")
            .setMaster("master")
            .setRelease("release/")
            .setHotfix("hotfix/")
            .setSupport("support/")
            .setVersiontag("version/")

    @Rule
    final TemporaryFolder folder = new TemporaryFolder()

    private JGitFlow jGitFlow
    private JGitFlowAccessor accessor
    private String head

    def setup() {
        jGitFlow = JGitFlow.getOrInit(folder.getRoot(), INIT_CTX)
        new File(folder.getRoot(), "test.txt").createNewFile()
        accessor = new JGitFlowAccessor(new JGitFlowWithConfig(jGitFlow))
        jGitFlow.git().add().addFilepattern("test.txt").call()
        head = jGitFlow.git().commit().setMessage("Test message").call().name() as String
    }

    def "getBranch returns checked out branch"() {
        when:
        jGitFlow.git().checkout().setName("test").setCreateBranch(true).call()

        then:
        accessor.branch == "test"
    }

    def "getRevisionId returns head commit id"() {
        expect:
        accessor.revisionId == head
    }

    def 'getUncommittedFilePaths works'() {
        expect:
        accessor.uncommittedFilePaths.isEmpty()

        when:
        new File(folder.getRoot(), "test.txt") << "changed"
        new File(folder.getRoot(), "test2.txt").createNewFile()
        then:
        accessor.uncommittedFilePaths == ['test.txt', 'test2.txt'] as Set
    }

    @Unroll
    def "flowConfig changes configuration for #property from #initialValue to #newValue"() {
        setup:
        final flowConfig = accessor.flowConfig

        expect:
        flowConfig[property] == initialValue

        when:
        flowConfig[property] = newValue
        then:
        flowConfig[property] == newValue

        where:
        property              | initialValue        | newValue
        'productionBranch'    | INIT_CTX.master     | 'myMaster'
        'developmentBranch'   | INIT_CTX.develop    | 'myDevelop'
        'featureBranchPrefix' | INIT_CTX.feature    | 'myFeature/'
        'releaseBranchPrefix' | INIT_CTX.release    | 'myRelease/'
        'hotfixBranchPrefix'  | INIT_CTX.hotfix     | 'myHotfix/'
        'supportBranchPrefix' | INIT_CTX.support    | 'support/'
        'versionTagPrefix'    | INIT_CTX.versiontag | 'myVersion/'
    }
}
