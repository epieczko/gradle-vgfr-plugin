package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.version.VersionTransformer

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@Subject(VersionTransformerFactory)
class VersionTransformerFactoryTest extends Specification {

    private final VersionTransformer mock = Mock(VersionTransformer)

    def "closure with no arguments returns null"() {
        expect:
        VersionTransformerFactory.create({ -> }) == null
    }

    def "closure with wrong argument type throws exception when transforming"() {
        setup:
        final transformer = VersionTransformerFactory.create({ String x, String y -> x + y })

        when:
        transformer.transform("1.2.3", 123)
        then:
        thrown(IllegalArgumentException)
    }

    @Unroll
    def "closure with #count arguments is invoked"() {
        setup:
        final source = "1.2.3"
        final args = ['x', 1].toArray()

        when:
        final transformer = VersionTransformerFactory.create(closure)
        and:
        final actual = transformer.transform(source, args)
        then:
        actual

        where:
        count | closure
        1     | { v -> v == '1.2.3' }
        2     | { v, a -> v == '1.2.3' && a == 'x' }
        3     | { v, a, b -> v == '1.2.3' && a == 'x' && b == 1 }
    }

}
