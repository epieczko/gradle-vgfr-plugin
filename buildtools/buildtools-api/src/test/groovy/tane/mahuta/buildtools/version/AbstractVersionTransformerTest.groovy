package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
@Subject(VersionTransformer.AbstractVersionTransformer)
class AbstractVersionTransformerTest extends Specification {

    private final VersionTransformer mock = Mock(VersionTransformer)

    private final VersionTransformer.AbstractVersionTransformer transformer = new VersionTransformer.AbstractVersionTransformer() {
        @Override
        Object doTransform(@Nonnull final Object version, @Nonnull final Object... args) {
            mock.transform(version, args)
        }
    }

    def "transform calls doTransform with non null values"() {
        setup:
        final version = Mock(Version)
        final args = [1,"2", 3l].toArray()
        final transformed = Mock(Version)

        when:
        final actual = transformer.transform(version, args)
        then:
        1 * mock.transform(version, args) >> transformed
        and:
        actual.is(transformed)
    }

    def "transform does not call doTransform with null value"() {
        when:
        final actual = transformer.transform(null)
        then:
        0 * mock.transform(_)
        and:
        actual == null
    }
}