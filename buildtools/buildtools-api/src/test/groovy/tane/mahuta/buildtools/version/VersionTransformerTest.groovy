package tane.mahuta.buildtools.version

import spock.lang.Specification

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
class VersionTransformerTest extends Specification {

    def 'decoration works'() {
        setup:
        final version = expected
        final transformer = new VersionTransformer() {
            @Override
            Object transform(@Nullable final Object v, @Nonnull final Object... args) {
                return version
            }
        }

        expect:
        transformer.decorate(new VersionTransformer() {
            @Override
            Object transform(@Nullable final Object v, @Nonnull final Object... args) {
                return v
            }
        }).transform(source) == expected


        where:
        source        | expected
        null          | null
        Mock(Version) | Mock(Version)
    }
}
