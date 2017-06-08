package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@Subject(DefaultSemanticBranchVersion)
class DefaultSemanticBranchVersionTest extends Specification {

    @Unroll
    def 'version #decorated in branch #branch is created correctly as #stringRepresentation'() {

        when: 'creating the branch version'
        final actual = DefaultSemanticBranchVersion.of(decorated, branch)
        then: 'the branch qualifier matches'
        actual.branchQualifier == branch
        and: 'the string representation matches'
        actual as String == stringRepresentation
        and: 'hash codes do not match'
        actual.hashCode() != decorated.hashCode()
        and: 'object is not equal to the decorated'
        actual != decorated
        and: 'creating it with the same parameters returns same instance'
        actual.is(DefaultSemanticBranchVersion.of(decorated, branch))

        where:
        decorated                                      | branch   | stringRepresentation
        DefaultSemanticVersion.parse("1.2.3")          | null     | "1.2.3"
        DefaultSemanticVersion.parse("1.2.3")          | "branch" | "1.2.3-branch"
        DefaultSemanticVersion.parse("1.2.3-SNAPSHOT") | null     | "1.2.3-SNAPSHOT"
        DefaultSemanticVersion.parse("1.2.3-SNAPSHOT") | "branch" | "1.2.3-branch-SNAPSHOT"

    }

}
