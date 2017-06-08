package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@Subject(VersionParser)
class VersionParserTest extends Specification {

    def 'decoration works'() {
        setup:
        final version = expected
        final parser = new VersionParser() {
            @Override
            Version parse(@Nullable Object source) {
                version
            }
        }

        expect:
        parser.decorate(new VersionParser() {
            @Override
            Version parse(@Nullable Object source) { source }
        }).parse(source) == expected


        where:
        source        | expected
        null          | null
        Mock(Version) | Mock(Version)
    }

}
