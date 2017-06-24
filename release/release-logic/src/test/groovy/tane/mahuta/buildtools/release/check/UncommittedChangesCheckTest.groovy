package tane.mahuta.buildtools.release.check

import spock.lang.Subject
import spock.lang.Unroll

/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@Subject(UncommittedChangesCheck)
class UncommittedChangesCheckTest extends AbstractReleaseStepSpecification {

    @Unroll
    def 'apply for #changes uncommitted changes adds #problems problems'() {
        setup:
        infrastructure.vcs.getUncommittedFilePaths() >> (['A'] * changes)
        when:
        UncommittedChangesCheck.instance.apply(artifactRelease, infrastructure)
        then:
        artifactRelease.getProblems().size() == problems
        where:
        changes | problems
        0       | 0
        1       | 1
        23      | 1
    }


    def 'description is set'() {
        expect:
        UncommittedChangesCheck.instance.description != null
    }

}
