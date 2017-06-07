package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * @author christian.heike@icloud.com
 * Created on 23.05.17.
 */
@Subject(DefaultSemanticVersion)
class DefaultSemanticVersionTest extends Specification {

    @Unroll
    def "parse #source to (#major,#minor,#micro,#qualifier)"() {
        setup:
        final expected = DefaultSemanticVersion.of(major, minor, micro, qualifier)

        when: 'parsing the version'
        def actual = DefaultSemanticVersion.parse(source)
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
        DefaultSemanticVersion.parse("x.y.z")
        then:
        thrown(IllegalArgumentException)
    }

    def "creating versions with the same parameters returns one instance"() {
        expect:
        DefaultSemanticVersion.parse("1.2.3-SNAPSHOT").is(DefaultSemanticVersion.of(1, 2, 3, "SNAPSHOT"))
    }

    @Unroll
    def "compare(#lhs, #rhs) = #expected / #expectedEquals"() {
        setup:
        final lhsVer = DefaultSemanticVersion.parse(lhs)
        final rhsVer = DefaultSemanticVersion.parse(rhs)

        expect:
        lhsVer <=> rhsVer == expected
        and:
        rhsVer <=> lhsVer == -1 * expected
        and:
        (lhs.hashCode() == rhs?.hashCode()) == expectedEquals
        and:
        lhs.equals(rhs) == expectedEquals

        where:
        lhs              | rhs              | expected | expectedEquals
        "1.2"            | "1.2.0"          | 0        | false
        "1.2"            | null             | 1        | false
        "1.2.0"          | "1.2.0"          | 0        | true
        "1.2-SNAPSHOT"   | "1.2.0-SNAPSHOT" | 0        | false
        "1.2.0-SNAPSHOT" | "1.2.0-SNAPSHOT" | 0        | true
        "1.2.3"          | "1.2.4"          | -1       | false
        "1.2.3"          | "1.3.4"          | -1       | false
        "1.2.3"          | "2.3.4"          | -1       | false
        "1.3.0-SNAPSHOT" | "1.3.0"          | -1       | false
        "1.3-SNAPSHOT"   | "1.3.0"          | -1       | false
    }

}
