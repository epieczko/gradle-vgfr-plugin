package tane.mahuta.buildtools.release.flow

import spock.lang.Subject
import tane.mahuta.buildtools.release.check.AbstractReleaseStepSpecification
/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@Subject(CommitReleaseVersionStep)
class CommitReleaseVersionStepTest extends AbstractReleaseStepSpecification {

    def "description returns non null"() {
        expect:
        CommitReleaseVersionStep.instance.description != null
    }

    def "apply issues the correct operations"() {
        when:
        CommitReleaseVersionStep.instance.apply(artifactRelease, infrastructure)
        then:
        1 * infrastructure.getBuildToolAdapter().setVersion("1.0.0")
        1 * infrastructure.getVersionStorage().store("1.0.0")
        1 * infrastructure.getVcs().commitFiles(_)
        1 * infrastructure.getVcs().push()
    }
}
