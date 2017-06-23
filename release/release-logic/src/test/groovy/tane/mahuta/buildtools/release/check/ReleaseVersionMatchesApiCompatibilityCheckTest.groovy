package tane.mahuta.buildtools.release.check

import spock.lang.Unroll
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder
import tane.mahuta.buildtools.dependency.ResolvedArtifactWithDependencies

/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
class ReleaseVersionMatchesApiCompatibilityCheckTest extends AbstractReleaseStepSpecification {

    private final apiReport = Mock(ApiCompatibilityReport)

    def setup() {
        final builder = Mock(ApiCompatibilityReportBuilder)
        builder./with.+/(_) >> builder
        builder.buildReport() >> apiReport
        infrastructure.getApiCompatibilityReportBuilderFactory().builder() >> builder

    }

    @Unroll
    def '#actualReleaseVersion vs #minReleaseVersion produces #problems problems'() {
        setup:
        final comparator = Mock(Comparator)
        when:
        ReleaseVersionMatchesApiCompatibilityCheck.instance.apply(artifactRelease, infrastructure)
        then:
        1 * infrastructure.versionHandler.toReleaseVersion(artifactRelease.descriptor.version) >> actualReleaseVersion
        1 * infrastructure.versionHandler.toReleaseVersionWithReport(artifactRelease.descriptor.version, apiReport) >> minReleaseVersion
        1 * infrastructure.versionHandler.parse(_, folder.root) >> { it[0] }
        1 * infrastructure.versionHandler.getComparator() >> comparator
        1 * comparator.compare(_, _) >> { s1, s2 -> s1 <=> s2 }
        1 * infrastructure.artifactResolver.resolveLastReleaseArtifact(_) >> stubReleaseArtifact()
        1 * artifactRelease.getClasspathDependencies() >> ([] as Set)

        and:
        artifactRelease.problems.size() == problems

        where:
        actualReleaseVersion | minReleaseVersion | problems
        '1.2.3'              | '1.2.3'           | 0
        '1.2.3'              | '1.2.4'           | 1
        '3.2.3'              | '1.2.4'           | 0

    }

    def 'release does not contain a file does not produce an error'() {
        when:
        artifactRelease.getLocalFile() >> null
        and:
        ReleaseVersionMatchesApiCompatibilityCheck.instance.apply(artifactRelease, infrastructure)
        then:
        0 * infrastructure.versionHandler./.+/(_)
        0 * infrastructure.artifactResolver./.+/(_)

        and:
        artifactRelease.problems.isEmpty()
    }

    protected ResolvedArtifactWithDependencies stubReleaseArtifact() {
        final result = Stub(ResolvedArtifactWithDependencies)
        result.getClasspathDependencies() >> ([] as Set)
        result.getLocalFile() >> (new File(folder.root, "other.jar"))
        result
    }

}
