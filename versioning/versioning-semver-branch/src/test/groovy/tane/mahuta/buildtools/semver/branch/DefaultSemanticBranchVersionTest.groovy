package tane.mahuta.buildtools.semver.branch

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.semver.DefaultSemanticVersion

/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@Subject(DefaultSemanticBranchVersion)
class DefaultSemanticBranchVersionTest extends Specification {

    @Unroll
    def 'version (#major, #minor, #micro, #branch, #qualifier) in is created correctly as #stringRepresentation'() {

        when: 'creating the branch version'
        final actual = new DefaultSemanticBranchVersion(major, minor, micro, { -> branch }, qualifier)
        then: 'the branch qualifier matches'
        actual.branchQualifier == branch
        and: 'the string representation matches'
        actual as String == stringRepresentation

        where:
        major | minor | micro | qualifier  | branch   | stringRepresentation
        1     | 2     | 3     | null       | null     | "1.2.3"
        1     | 2     | 3     | null       | "branch" | "1.2.3-branch"
        1     | 2     | 3     | "SNAPSHOT" | null     | "1.2.3-SNAPSHOT"
        1     | 2     | 3     | "SNAPSHOT" | "branch" | "1.2.3-branch-SNAPSHOT"
    }

    @Unroll
    def 'comparing #lhs and #rhs returns #expected'() {
        expect:
        lhs <=> rhs == expected
        and:
        lhs.hashCode() != rhs.hashCode()
        and:
        !lhs.equals(rhs)
        and:
        lhs.equals(lhs)

        where:
        lhs                                        | rhs                                        | expected | equal
        semVerBranch(1, 2, 3, null, null)          | semVer(1, 2, 3, null)                      | 0        | false
        semVerBranch(1, 2, 3, null, "blubb")       | semVer(1, 2, 3, null)                      | -1       | false
        semVerBranch(1, 2, 3, null, null)          | semVer(1, 2, 3, "SNAPSHOT")                | 1        | false
        semVerBranch(1, 2, 3, null, "blubb")       | semVer(1, 2, 3, "SNAPSHOT")                | 1        | false
        semVerBranch(1, 2, 3, "SNAPSHOT", null)    | semVer(1, 2, 3, "SNAPSHOT")                | 0        | false
        semVerBranch(1, 2, 3, "SNAPSHOT", "blubb") | semVer(1, 2, 3, "SNAPSHOT")                | -1       | false
        semVerBranch(1, 2, 3, "SNAPSHOT", null)    | semVerBranch(1, 2, 3, "SNAPSHOT", null)    | 0        | false
        semVerBranch(1, 2, 3, "SNAPSHOT", "blubb") | semVerBranch(1, 2, 3, "SNAPSHOT", null)    | -1       | false
        semVerBranch(1, 2, 3, "SNAPSHOT", null)    | semVerBranch(1, 2, 3, "SNAPSHOT", "blubb") | 1        | false
    }

    protected static DefaultSemanticBranchVersion semVerBranch(major, minor, micro, qualifier, branch) {
        new DefaultSemanticBranchVersion(major, minor, micro, { -> branch }, qualifier)
    }

    protected static DefaultSemanticVersion semVer(major, minor, micro, qualifier) {
        new DefaultSemanticVersion(major, minor, micro, qualifier)
    }

}
