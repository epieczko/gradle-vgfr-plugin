package tane.mahuta.buildtools.release.flow

import spock.lang.Subject
import tane.mahuta.buildtools.release.check.AbstractReleaseStepSpecification
/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@Subject(StartReleaseStep)
class StartReleaseStepTest extends AbstractReleaseStepSpecification {

    def "description returns non null"() {
        expect:
        StartReleaseStep.instance.description != null
    }

    def "apply issues the correct operations"() {
        when:
        StartReleaseStep.instance.apply(artifactRelease, infrastructure)
        then:
        1 * infrastructure.getVcs().startReleaseBranch("1.0.0")
    }

}
