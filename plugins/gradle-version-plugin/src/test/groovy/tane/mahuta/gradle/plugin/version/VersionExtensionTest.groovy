package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionParser
import tane.mahuta.buildtools.version.VersionStorage
import tane.mahuta.buildtools.version.VersionTransformer

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@Subject(VersionExtension)
class VersionExtensionTest extends Specification {

    @Subject
    private final VersionExtension extension = new VersionExtension()

    private final parser = Mock(VersionParser)
    private final version = Mock(Version)
    private final storage = Mock(VersionStorage)

    def 'default version is undefined'() {
        expect:
        extension as String == 'undefined'
    }

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

    def 'not defined transformer throws exception'() {
        when:
        extension.toNextSnapshot
        then:
        thrown(MissingPropertyException)

        when:
        extension.toNextSnapshot()
        then:
        thrown(MissingMethodException)
    }

    @Unroll
    def 'defining transformer property from #name throws exception'() {
        when:
        extension.toNextSnapshot = definition
        then:
        thrown(IllegalArgumentException)

        where:
        name              | definition
        'invalid closure' | { -> }
        'string'          | 'a'
        'array'           | [].toArray()
    }

    @Unroll
    def 'defining a transformer from #name and using it'() {
        setup:
        extension.rawVersion = "1.2.3"
        when:
        extension.transformerThing = definition
        then:
        extension.transformerThing instanceof VersionTransformer
        and:
        extension.transformerThing("x") == "1.2.4"

        where:
        name                      | definition
        'closure with single arg' | { v -> v == "1.2.3" ? "1.2.4" : null }
        'closure with all args' | { v, args -> v == "1.2.3" && args[0] == "x" ? "1.2.4" : null }
        'version transformer' | (new VersionTransformer() {
            @Override
            Object transform(@Nullable Object version, @Nonnull Object... args) {
                version == "1.2.3" && args[0] == "x" ? "1.2.4" : null
            }
        })
    }

}
