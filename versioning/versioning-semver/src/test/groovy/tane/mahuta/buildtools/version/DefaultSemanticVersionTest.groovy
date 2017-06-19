package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.version.DefaultSemanticVersion
import tane.mahuta.buildtools.version.DefaultSemanticVersionParser

/**
 * @author christian.heike@icloud.com
 * Created on 23.05.17.
 */
@Subject(DefaultSemanticVersion)
class DefaultSemanticVersionTest extends Specification {

    @Unroll
    def "compare(#lhs, #rhs) = #expected"() {
        setup:
        final lhsVer = lhs ? DefaultSemanticVersionParser.instance.parse(lhs, null) : null
        final rhsVer = rhs ? DefaultSemanticVersionParser.instance.parse(rhs, null) : null

        expect:
        lhsVer <=> rhsVer == expected
        and:
        rhsVer <=> lhsVer == -1 * expected
        and:
        (lhs.hashCode() == rhs?.hashCode()) == expectedEquals
        and:
        (lhs.equals(rhs)) == expectedEquals

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
