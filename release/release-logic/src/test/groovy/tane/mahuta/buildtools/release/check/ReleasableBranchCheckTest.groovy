package tane.mahuta.buildtools.release.check

import spock.lang.Subject
import spock.lang.Unroll

/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@Subject(ReleasableBranchCheck)
class ReleasableBranchCheckTest extends AbstractReleaseStepSpecification {

    @Unroll
    def "apply for branch #branch adds #problems problems"() {
        setup:
        infrastructure.vcs.branch >> branch
        when:
        ReleasableBranchCheck.instance.apply(artifactRelease, infrastructure)
        then:
        artifactRelease.getProblems().size() == problems
        where:
        branch          | problems
        'master'        | 1
        'development'   | 0
        'blubb'         | 1
        'release/1.2.3' | 0
        'hotfix/1.2.3'  | 0
        'support/xy'    | 0
    }
}
