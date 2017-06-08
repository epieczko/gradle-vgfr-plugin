package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionTransformer

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
class VersionTransformerFactoryTest extends Specification {

    private final VersionTransformer mock = Mock(VersionTransformer)

    def "closure is invoked"() {
        setup:
        final source = Mock(Version)
        final args = ['x',1,2l, new Object()].toArray()
        final transformed = Mock(Version)

        when:
        final transformer = VersionTransformerFactory.create{ v, arguments -> mock.transform(v, arguments) }
        and:
        final actual = transformer.transform(source, args)
        then:
        1 * mock.transform(source, args) >> transformed
        and:
        actual.is(transformed)
    }

}
