package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.version.Version

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@Subject(VersionParserFactory)
class VersionParserFactoryTest extends Specification {

    def 'parse delegates to closure'() {
        setup:
        final version = Mock(Version)
        final parser = VersionParserFactory.create { it -> version}

        expect:
        parser.parse("1.2.3") == version
    }

}
