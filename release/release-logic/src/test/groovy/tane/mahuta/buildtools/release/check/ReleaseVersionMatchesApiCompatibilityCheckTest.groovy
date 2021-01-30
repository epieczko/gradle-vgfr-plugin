package tane.mahuta.buildtools.release.check

import spock.lang.Unroll
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.ArtifactWithClasspath

/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
class ReleaseVersionMatchesApiCompatibilityCheckTest extends AbstractReleaseStepSpecification {

    private final apiReport = Mock(ApiCompatibilityReport)

    def setup() {
        final builder = Mock(ApiCompatibilityReportBuilder)
        builder./with.+/(_) >> builder
        builder.buildReport(_) >> apiReport
        infrastructure.getApiCompatibilityReportBuilderFactory().builder() >> builder

    }

    @Unroll
    def '#actualReleaseVersion vs #minReleaseVersion produces #problems problems'() {
        setup:
        final comparator = Mock(Comparator)
        final invocations = lastRelease != null ? 1 : 0
        when:
        ReleaseVersionMatchesApiCompatibilityCheck.instance.apply(artifactRelease, infrastructure)
        then:
        (1 + invocations) * infrastructure.versionHandler.parse(_, folder.root) >> { it[0] }
        1 * infrastructure.artifactResolver.resolveLastReleaseArtifact(_) >> stubReleaseArtifact(lastRelease)
        1 * infrastructure.versionHandler.toReleaseVersion(artifactRelease.descriptor.version) >> actualReleaseVersion
        invocations * infrastructure.versionHandler.toNextReleaseVersion(lastRelease, apiReport) >> minReleaseVersion
        invocations * infrastructure.versionHandler.getComparator() >> comparator
        invocations * comparator.compare(_, _) >> { s1, s2 -> s1 <=> s2 }
        invocations * artifactRelease.getClasspathDependencies() >> ([] as Set)

        and:
        artifactRelease.problems.size() == problems

        where:
        actualReleaseVersion | lastRelease | minReleaseVersion | problems
        '1.2.3'              | '1.2.2'     | '1.2.3'           | 0
        '1.2.3'              | '1.2.0'     | '1.3.0'           | 1
        '3.2.3'              | '1.2.3'     | '1.2.4'           | 0
        '3.2.3'              | null        | '1.2.4'           | 0

    }

    def 'release does not contain a file does not produce an error'() {
        when:
        artifactRelease.getLocalFile() >> null
        and:
        ReleaseVersionMatchesApiCompatibilityCheck.instance.apply(artifactRelease, infrastructure)
        then:
        0 * infrastructure.versionHandler.toNextReleaseVersion(_, _)
        0 * infrastructure.artifactResolver./.+/(_)

        and:
        artifactRelease.problems.isEmpty()
    }

    def 'description is set'() {
        expect:
        ReleaseVersionMatchesApiCompatibilityCheck.instance.description != null
    }

    protected ArtifactWithClasspath stubReleaseArtifact(final String lastRelease) {
        if (lastRelease == null) {
            return null // No release
        }
        final result = Stub(ArtifactWithClasspath)
        result.getClasspathDependencies() >> ([] as Set)
        result.getLocalFile() >> (new File(folder.root, "other.jar"))
        result.getDescriptor() >> Stub(GAVCDescriptor) {
            getGroup() >> artifactRelease.descriptor.group
            getArtifact() >> artifactRelease.descriptor.artifact
            getVersion() >> lastRelease
        }
        result
    }

}
