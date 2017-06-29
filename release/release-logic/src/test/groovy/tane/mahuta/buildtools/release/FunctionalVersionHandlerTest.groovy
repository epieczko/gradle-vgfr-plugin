package tane.mahuta.buildtools.release

import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport
import tane.mahuta.buildtools.version.VersionParser
import tane.mahuta.buildtools.version.VersionStorage

import java.util.function.BiFunction
import java.util.function.Function

/**
 * @author christian.heike@icloud.com
 * Created on 20.06.17.
 */
@Subject(FunctionalVersionHandler)
class FunctionalVersionHandlerTest extends Specification {

    def "builds correctly"() {
        setup:
        final releaseReportMock = Mock(BiFunction)
        final releaseMock = Mock(Function)
        final nextDevelopmentMock = Mock(Function)
        final parser = Mock(VersionParser)
        final comparator = Mock(Comparator)
        final storage = Mock(VersionStorage)
        final handler = FunctionalVersionHandler.builder()
                .parser(parser)
                .toReleaseVersionWithReportHandler(releaseReportMock)
                .toReleaseVersionHandler(releaseMock)
                .toNextDevelopmentVersionHandler(nextDevelopmentMock)
                .comparator(comparator)
                .storage(storage)
                .build()

        when:
        def actual = handler.parse("YY", new File('.'))
        then:
        parser.parse(_, _) >> "Z"
        and:
        actual == "Z"

        when:
        actual = handler.toReleaseVersion("Z")
        then:
        releaseMock.apply("Z") >> "A"
        and:
        actual == "A"

        when:
        actual = handler.toNextDevelopmentVersion("Z")
        then:
        nextDevelopmentMock.apply("Z") >> "B"
        and:
        actual == "B"

        when:
        actual = handler.toNextReleaseVersion("Z", Mock(ApiCompatibilityReport))
        then:
        releaseReportMock.apply("Z", _) >> "Q"
        and:
        actual == "Q"

        expect:
        handler.comparator.is(comparator)
        handler.storage.is(storage)
    }

}
