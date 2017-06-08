package tane.mahuta.gradle.plugin.version.transform

import spock.lang.Specification
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionTransformer

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
class AbstractVersionTransformerTest extends Specification {

    private final VersionTransformer mock = Mock(VersionTransformer)

    private
    final VersionTransformer.AbstractVersionTransformer transformer = new VersionTransformer.AbstractVersionTransformer() {
        @Override
        Object doTransform(@Nonnull final Object version, @Nonnull Object... args) {
            mock.transform(version, args)
        }
    }

    def "transform calls doTransform with non null values"() {
        setup:
        final args = new Object[0]
        final version = Mock(Version)
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
        0 * mock.transform(_, _)
        and:
        actual == null
    }
}