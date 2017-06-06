package tane.mahuta.build.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * @author christian.heike@icloud.com
 * Created on 23.05.17.
 */
@Subject(SemanticVersion)
class SemanticVersionTest extends Specification {

    @Unroll
    def "parse #source to (#major,#minor,#micro,#qualifier)"() {
        setup:
        final expected = SemanticVersion.of(major, minor, micro, qualifier)

        when: 'parsing the version'
        def actual = SemanticVersion.parse(source)
        then: 'the version equals the expected'
        actual == expected
        and: 'the string representation does not change'
        actual.toString() == source
        and: 'the string representation does not change'
        actual.toStorable() == source
        and:
        actual.major == expected.major
        and:
        actual.minor == expected.minor
        and:
        actual.micro == expected.micro
        and:
        actual.qualifier == expected.qualifier

        where:
        source                 | major | minor | micro | qualifier
        "1.2"                  | 1     | 2     | null  | null
        "1.2.3"                | 1     | 2     | 3     | null
        "1.2.3-SNAPSHOT"       | 1     | 2     | 3     | "SNAPSHOT"
        "1.2.3-BRANCH-RELEASE" | 1     | 2     | 3     | "BRANCH-RELEASE"
    }

    def "parsing non matching pattern throws exception"() {
        when:
        SemanticVersion.parse("x.y.z")
        then:
        thrown(IllegalArgumentException)
    }

    def "creating versions with the same parameters returns one instance"() {
        expect:
        SemanticVersion.parse("1.2.3-SNAPSHOT").is(SemanticVersion.of(1, 2, 3, "SNAPSHOT"))
    }

    @Unroll
    def "compare(#lhs, #rhs) = #expected"() {
        setup:
        final lhsVer = SemanticVersion.parse(lhs)
        final rhsVer = SemanticVersion.parse(rhs)

        expect:
        lhsVer <=> rhsVer == expected
        and:
        rhsVer <=> lhsVer == -1 * expected

        where:
        lhs              | rhs              | expected
        "1.2"            | "1.2.0"          | 0
        "1.2"            | null             | 1
        "1.2.0"          | "1.2.0"          | 0
        "1.2-SNAPSHOT"   | "1.2.0-SNAPSHOT" | 0
        "1.2.0-SNAPSHOT" | "1.2.0-SNAPSHOT" | 0
        "1.2.3"          | "1.2.4"          | -1
        "1.2.3"          | "1.3.4"          | -1
        "1.2.3"          | "2.3.4"          | -1
        "1.3.0-SNAPSHOT" | "1.3.0"          | -1
        "1.3-SNAPSHOT"   | "1.3.0"          | -1
    }

}
