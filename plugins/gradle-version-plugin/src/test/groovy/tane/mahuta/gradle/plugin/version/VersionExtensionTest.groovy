package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionParser
import tane.mahuta.buildtools.version.VersionStorage
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

    @Unroll
    def 'comparing 1.2.3 and #version results in #expected'() {
        when:
        extension.setRawVersion("1.2.3")
        then:
        extension <=> version == expected
        where:
        version | expected
        "1.2.3" | 0
        "1.2.4" | -1
        "1.2.2" | 1
    }

    @Unroll
    def 'comparing Stub(1.2.3).toString() and #version results in #expected'() {
        when:
        extension.setRawVersion("1.2.3")
        extension.setParser(VersionParserFactory.create { v ->
            Stub(Version) {
                toString() >> "1.2.3"
            }
        })
        then:
        extension <=> version == expected
        where:
        version | expected
        "1.2.3" | 0
        "1.2.4" | -1
        "1.2.2" | 1
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

    def 'not defined method throws exception'() {
        setup:
        extension.setParser({ v -> Mock(Version)})
        when:
        extension.toNextSnapshot()
        then:
        thrown(MissingMethodException)
    }

    def 'not defined version throws exception when invoking methods or properties'() {
        expect:
        extension.platsch == null
        when:
        extension.platsch = 'A'
        then:
        thrown(MissingPropertyException)

        when:
        extension.blubb()
        then:
        thrown(MissingMethodException)
    }

    def 'setting a raw version object uses it directly without parsing'() {
        setup:
        final parserMock = Mock(VersionParser)
        extension.setParser(parserMock)
        final versionStub = Stub(Version)

        when:
        extension.setRawVersion(versionStub)
        then:
        0 * parserMock.parse(_)
        and:
        extension as String == versionStub as String
    }

    def 'delegates properties and methods'() {
        setup:
        final mock = Mock(TestVersion)
        extension.setRawVersion(mock)

        when:
        extension.a = 'A'
        then:
        1 * mock.setA('A')

        when:
        final actual = extension.a
        then:
        1 * mock.getA() >> 'A'
        and:
        actual == 'A'
    }

    private static interface TestVersion extends Version {
        void setA(String a)
        String getA()
    }

}
