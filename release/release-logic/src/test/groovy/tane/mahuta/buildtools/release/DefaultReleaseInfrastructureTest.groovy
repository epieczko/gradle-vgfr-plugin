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
        final buildTool = Mock(BuildToolAdapter)

        when:
        final actual = DefaultReleaseInfrastructure.builder()
                .apiCompatibilityReportBuilderFactory(reportFactory)
                .artifactResolver(resolver)
                .vcs(vcs)
                .buildToolAdapter(buildTool)
                .versionStorage(storage)
                .versionHandler(handler).build()
        then:
        actual.apiCompatibilityReportBuilderFactory.is(reportFactory)
        actual.artifactResolver.is(resolver)
        actual.vcs.is(vcs)
        actual.versionHandler.is(handler)
        actual.versionStorage.is(storage)
        actual.buildToolAdapter.is(buildTool)
    }

}
