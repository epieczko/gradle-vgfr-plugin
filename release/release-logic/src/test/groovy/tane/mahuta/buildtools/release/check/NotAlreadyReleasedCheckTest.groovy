package tane.mahuta.buildtools.release.check

import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.dependency.ArtifactWithClasspath
/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@Subject(NotAlreadyReleasedCheck)
class NotAlreadyReleasedCheckTest extends AbstractReleaseStepSpecification {

    @Unroll
    def 'released version = #released adds #problems problems'() {
        setup:
        final descriptor = artifactRelease.descriptor
        infrastructure.versionHandler.parse(descriptor.version, folder.root) >> descriptor.version
        final releaseVersion = descriptor.version.replace("-SNAPSHOT", "")
        infrastructure.versionHandler.toReleaseVersion(descriptor.version) >> releaseVersion

        when:
        NotAlreadyReleasedCheck.instance.apply(artifactRelease, infrastructure)
        then:
        1 * infrastructure.artifactResolver.resolveArtifact({
            it.group == descriptor.group && it.artifact == descriptor.artifact && it.version == releaseVersion && it.classifier == descriptor.classifier
        }) >> (released ? Mock(ArtifactWithClasspath) : null)
        and:
        artifactRelease.getProblems().size() == problems

        where:
        released | problems
        false    | 0
        true     | 1
    }

    def 'description is set'() {
        expect:
        NotAlreadyReleasedCheck.instance.description != null
    }

}
