package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.version.DefaultSemanticVersion
import tane.mahuta.buildtools.version.DefaultSemanticVersionParser

/**
 * @author christian.heike@icloud.com
 * Created on 14.06.17.
 */
@Subject(DefaultSemanticVersionParser)
class DefaultSemanticVersionParserTest extends Specification {

    @Unroll
    def "parse #source to (#major,#minor,#micro,#qualifier)"() {
        setup:
        final expected = new DefaultSemanticVersion(major, minor, micro, qualifier)

        when: 'parsing the version'
        def actual = DefaultSemanticVersionParser.instance.parse(source, null)
        then: 'the version equals the expected'
        actual == expected
        and: 'the string representation does not change'
        actual as String == source
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
        DefaultSemanticVersionParser.instance.parse("x.y.z", null)
        then:
        thrown(IllegalArgumentException)
    }

}
