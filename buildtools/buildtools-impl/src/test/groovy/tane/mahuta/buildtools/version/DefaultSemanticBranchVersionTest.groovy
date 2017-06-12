package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static tane.mahuta.buildtools.version.DefaultSemanticBranchVersion.parseWithBranchName
import static tane.mahuta.buildtools.version.DefaultSemanticVersion.parse

/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@Subject(DefaultSemanticBranchVersion)
class DefaultSemanticBranchVersionTest extends Specification {

    @Unroll
    def 'version #decorated in branch #branch is created correctly as #stringRepresentation'() {

        when: 'creating the branch version'
        final actual = new DefaultSemanticBranchVersion(decorated, { -> branch })
        then: 'the branch qualifier matches'
        actual.branchQualifier == branch
        and: 'the string representation matches'
        actual as String == stringRepresentation
        and: 'hash codes do not match'
        actual.hashCode() != decorated.hashCode()
        and: 'object equality depends on branch qualifier'
        !actual.equals(decorated)
        and: 'comparison does not fail'
        actual <=> decorated == branch != null ? -1 : 0

        where:
        decorated               | branch   | stringRepresentation
        parse("1.2.3")          | null     | "1.2.3"
        parse("1.2.3")          | "branch" | "1.2.3-branch"
        parse("1.2.3-SNAPSHOT") | null     | "1.2.3-SNAPSHOT"
        parse("1.2.3-SNAPSHOT") | "branch" | "1.2.3-branch-SNAPSHOT"
    }

    @Unroll
    def 'comparing #lhs and #rhs returns #expected'() {
        expect:
        lhs <=> rhs == expected

        where:
        lhs                                            | rhs                                            | expected
        parseWithBranchName("1.2.3", null)             | parse("1.2.3")                                 | 0
        parseWithBranchName("1.2.3", "blubb")          | parse("1.2.3")                                 | -1
        parseWithBranchName("1.2.3", null)             | parse("1.2.3-SNAPSHOT")                        | 1
        parseWithBranchName("1.2.3", "blubb")          | parse("1.2.3-SNAPSHOT")                        | 1
        parseWithBranchName("1.2.3-SNAPSHOT", null)    | parse("1.2.3-SNAPSHOT")                        | 0
        parseWithBranchName("1.2.3-SNAPSHOT", "blubb") | parse("1.2.3-SNAPSHOT")                        | -1
        parseWithBranchName("1.2.3-SNAPSHOT", null)    | parseWithBranchName("1.2.3-SNAPSHOT", null)    | 0
        parseWithBranchName("1.2.3-SNAPSHOT", "blubb") | parseWithBranchName("1.2.3-SNAPSHOT", null)    | -1
        parseWithBranchName("1.2.3-SNAPSHOT", null)    | parseWithBranchName("1.2.3-SNAPSHOT", "blubb") | 1
    }

    @Unroll
    def "(#source).toRelease() returns #expected"() {
        setup:
        final lhsVer = DefaultSemanticBranchVersion.parseWithBranchName(source, branch)
        final rhsVer = DefaultSemanticBranchVersion.parseWithBranchName(expected, branch)

        expect:
        lhsVer.toRelease() == rhsVer

        where:
        source  | branch | expected
        "1.2.3" | null   | "1.2.3"
        "1.2.3" | "A"    | "1.2.3"
    }

    @Unroll
    def "(#source).toNextSnapshot(#level) returns #expected"() {
        setup:
        final lhsVer = DefaultSemanticBranchVersion.parseWithBranchName(source, branch)
        final rhsVer = DefaultSemanticBranchVersion.parseWithBranchName(expected, branch)

        expect:
        lhsVer.toNextSnapshot(level) == rhsVer

        where:
        source  | branch | level                              | expected
        "1.2.3" | null   | ChangeLevel.IMPLEMENTATION_CHANGED | "1.2.4-SNAPSHOT"
        "1.2.3" | "B"    | ChangeLevel.IMPLEMENTATION_CHANGED | "1.2.4-SNAPSHOT"
    }
}
