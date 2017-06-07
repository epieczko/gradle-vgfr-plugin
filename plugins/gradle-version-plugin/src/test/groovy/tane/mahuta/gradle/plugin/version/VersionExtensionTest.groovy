package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionParser
import tane.mahuta.buildtools.version.VersionStorage

/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
class VersionExtensionTest extends Specification {

    @Subject
    private final VersionExtension extension = new VersionExtension()
    private final parser = Mock(VersionParser)
    private final version = Mock(Version)
    private final storage = Mock(VersionStorage)

    def 'setting the parser parses the version'() {
        setup: 'mocking a parser and stubbing the parsed version'
        extension.setRawVersion("1.2.3")

        when: 'setting a parser'
        extension.parser = parser
        then: 'it is set correctly'
        extension.parser.is(parser)
        and: 'the version parser is being called'
        1 * parser.parse("1.2.3") >> version
        and: 'and the extension equals the parsed version'
        extension == version
        and:
        extension as String == version as String
    }

    def 'setting a version after setting a parser'() {
        setup: 'mocking the version and setting the parser'
        extension.parser = parser

        when: 'setting the raw version'
        extension.rawVersion = "1.2.3"
        then: 'the version parser is being called'
        1 * parser.parse("1.2.3") >> version
        and: 'and the extension equals the parsed version'
        extension == version
        and:
        extension as String == version as String
    }

    def 'version storage can be used'() {
        setup: 'adding a parser'
        extension.parser = parser

        when: 'the storage is set'
        extension.storage = storage
        and: 'load is invoked again'
        extension.load()
        then: 'the version is loaded from the storage'
        2 * storage.load() >> "1.2.3"
        and: 'the parser is invoked'
        2 * parser.parse("1.2.3") >> version

        when: 'storing the version'
        extension.store()
        then: 'the version is converted to a storable one'
        1 * version.toStorable() >> "1.2.3"
        and: 'being stored'
        1 * storage.store("1.2.3")
    }

}
