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

    private final VersionTransformer.AbstractVersionTransformer transformer = new VersionTransformer.AbstractVersionTransformer<Version, Version>() {
        @Override
        Version doTransform(@Nonnull final Version version) {
            mock.transform(version)
        }
    }

    def "transform calls doTransform with non null values"() {
        setup:
        final version = Mock(Version)
        final transformed = Mock(Version)

        when:
        final actual = transformer.transform(version)
        then:
        1 * mock.transform(version) >> transformed
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