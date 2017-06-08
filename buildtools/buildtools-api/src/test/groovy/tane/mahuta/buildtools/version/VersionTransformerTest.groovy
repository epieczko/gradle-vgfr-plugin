package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@Subject(VersionTransformer)
class VersionTransformerTest extends Specification {

    def 'decoration works'() {
        setup:
        final version = expected
        final expectedArgs = new Object[0]
        final transformer = new VersionTransformer() {
            @Override
            Object transform(@Nullable final Object v, @Nonnull final Object... args) {
                expectedArgs.is(args) ? version : null
            }
        }

        expect:
        transformer.decorate(new VersionTransformer() {
            @Override
            Object transform(@Nullable final Object v, @Nonnull final Object... args) {
                expectedArgs.is(args) ? v: null
            }
        }).transform(source, expectedArgs) == expected


        where:
        source        | expected
        null          | null
        Mock(Version) | Mock(Version)
    }
}
