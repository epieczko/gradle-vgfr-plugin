package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import spock.lang.Subject

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@Subject(VersionParserFactory)
class VersionParserFactoryTest extends Specification {

    def 'parse delegates to closure'() {
        setup:
        final expected = "1.2.4"
        final parser = VersionParserFactory.create { v, d -> expected }

        expect:
        parser.parse("1.2.3", null) == expected
    }

}
