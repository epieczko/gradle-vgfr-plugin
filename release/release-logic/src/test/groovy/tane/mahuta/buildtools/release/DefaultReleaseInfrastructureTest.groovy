package tane.mahuta.buildtools.release

import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder
import tane.mahuta.buildtools.dependency.ArtifactResolver
import tane.mahuta.buildtools.vcs.VcsAccessor
import tane.mahuta.buildtools.version.VersionStorage

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@Subject(DefaultReleaseInfrastructure)
class DefaultReleaseInfrastructureTest extends Specification {

    def 'builder sets args'() {
        setup:
        final handler = Mock(VersionHandler)
        final reportFactory = Mock(ApiCompatibilityReportBuilder.Factory)
        final resolver = Mock(ArtifactResolver)
        final vcs = Mock(VcsAccessor)
        final storage = Mock(VersionStorage)

        when:
        final actual = DefaultReleaseInfrastructure.builder()
                .apiCompatibilityReportBuilderFactory(reportFactory)
                .artifactResolver(resolver)
                .vcs(vcs)
                .versionStorage(storage)
                .versionHandler(handler).build()
        then:
        actual.apiCompatibilityReportBuilderFactory.is(reportFactory)
        and:
        actual.artifactResolver.is(resolver)
        and:
        actual.vcs.is(vcs)
        and:
        actual.versionHandler.is(handler)
        and:
        actual.versionStorage.is(storage)
    }

}
