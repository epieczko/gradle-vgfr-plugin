package tane.mahuta.buildtools.release.flow

import spock.lang.Subject
import tane.mahuta.buildtools.release.check.AbstractReleaseStepSpecification
/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@Subject(RunReleaseBuildStep)
class RunReleaseBuildStepTest extends AbstractReleaseStepSpecification {

    def "description returns non null"() {
        expect:
        RunReleaseBuildStep.instance.description != null
    }

    def "apply issues the correct operations"() {
        when:
        RunReleaseBuildStep.instance.apply(artifactRelease, infrastructure)
        then:
        1 * infrastructure.getBuildToolAdapter().buildRelease()
    }

}
