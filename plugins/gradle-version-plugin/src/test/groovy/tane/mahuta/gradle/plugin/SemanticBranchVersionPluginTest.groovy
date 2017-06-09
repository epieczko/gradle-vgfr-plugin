package tane.mahuta.gradle.plugin

import org.eclipse.jgit.api.Git
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.version.DefaultSemanticBranchVersion
import tane.mahuta.buildtools.version.DefaultSemanticVersion
import tane.mahuta.gradle.plugin.version.transform.ChangeLevel

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@Subject(SemanticBranchVersionPlugin)
class SemanticBranchVersionPluginTest extends Specification {

    @Rule
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    private Git git

    def setup() {
        git = Git.init().setDirectory(projectBuilder.root).call()
        new File(projectBuilder.root, "test.txt").text = "test"
        git.commit().setAll(true).setMessage("test commit").call()
        projectBuilder.project.version = '1.0.0'
        projectBuilder.project.apply plugin: SemanticBranchVersionPlugin
    }

    @Unroll
    def 'toSnapshot of #version is #versionString'() {
        setup:
        if (git.repository.branch != branch) {
            git.checkout().setName(branch).setCreateBranch(true).call()
        }
        projectBuilder.project.version = version
        final semanticVersionString = branchQualifier != null ? versionString.replace("-${branchQualifier}", "") : versionString
        final semanticVersion = DefaultSemanticVersion.parse(semanticVersionString)
        final expectedVersion = DefaultSemanticBranchVersion.of(semanticVersion, branchQualifier)

        when:
        final actual = projectBuilder.project.version.toNextSnapshot(ChangeLevel.API_INCOMPATIBILITY)
        then:
        actual == expectedVersion
        and:
        actual as String == versionString

        where:
        branch              | version | branchQualifier | versionString
        'master'            | '1.2.3' | null            | '2.2.3-SNAPSHOT'
        'feature/bla/blubb' | '1.2.3' | 'bla_blubb'     | '2.2.3-bla_blubb-SNAPSHOT'
        'support/platsch'   | '1.2.3' | 'platsch'       | '2.2.3-platsch-SNAPSHOT'
        'hotfix/1.2.3'      | '1.2.3' | null            | '2.2.3-SNAPSHOT'
        'release/1.2.3'     | '1.2.3' | null            | '2.2.3-SNAPSHOT'
    }

    @Unroll
    def 'toRelease of #version is #versionString'() {
        setup:
        if (git.repository.branch != branch) {
            git.checkout().setName(branch).setCreateBranch(true).call()
        }
        projectBuilder.project.version = version
        final semanticVersion = DefaultSemanticVersion.parse(version.replace("-SNAPSHOT", ""))
        final expectedVersion = DefaultSemanticBranchVersion.of(semanticVersion, branchQualifier)

        when:
        final actual = projectBuilder.project.version.toRelease()
        then:
        actual == expectedVersion
        and:
        actual as String == versionString

        where:
        branch              | version          | branchQualifier | versionString
        'master'            | '1.2.3-SNAPSHOT' | null            | '1.2.3'
        'feature/bla/blubb' | '1.2.3-SNAPSHOT' | 'bla_blubb'     | '1.2.3-bla_blubb'
        'support/platsch'   | '1.2.3-SNAPSHOT' | 'platsch'       | '1.2.3-platsch'
        'hotfix/1.2.3'      | '1.2.3-SNAPSHOT' | null            | '1.2.3'
        'release/1.2.3'     | '1.2.3-SNAPSHOT' | null            | '1.2.3'
    }

    @Unroll
    def 'semantic branch version of #version in branch #branch is returns #versionString'() {
        setup:
        if (git.repository.branch != branch) {
            git.checkout().setName(branch).setCreateBranch(true).call()
        }
        projectBuilder.project.version = version
        final semanticVersion = DefaultSemanticVersion.parse(version)
        final expectedVersion = DefaultSemanticBranchVersion.of(semanticVersion, branchQualifier)

        expect:
        projectBuilder.project.version == semanticVersion
        and:
        projectBuilder.project.version == expectedVersion
        and:
        projectBuilder.project.version as String == versionString

        where:
        branch              | version          | branchQualifier | versionString
        'master'            | '1.2.3'          | null            | '1.2.3'
        'master'            | '1.2.3-SNAPSHOT' | null            | '1.2.3-SNAPSHOT'
        'feature/bla/blubb' | '1.2.3'          | 'bla_blubb'     | '1.2.3-bla_blubb'
        'feature/bla/blubb' | '1.2.3-SNAPSHOT' | 'bla_blubb'     | '1.2.3-bla_blubb-SNAPSHOT'
        'support/platsch'   | '1.2.3'          | 'platsch'       | '1.2.3-platsch'
        'support/platsch'   | '1.2.3-SNAPSHOT' | 'platsch'       | '1.2.3-platsch-SNAPSHOT'
        'hotfix/1.2.3'      | '1.2.3'          | null            | '1.2.3'
        'hotfix/1.2.3'      | '1.2.3-SNAPSHOT' | null            | '1.2.3-SNAPSHOT'
        'release/1.2.3'     | '1.2.3'          | null            | '1.2.3'
        'release/1.2.3'     | '1.2.3-SNAPSHOT' | null            | '1.2.3-SNAPSHOT'
    }

}
