package tane.mahuta.buildtools.release.flow

import spock.lang.Subject
import tane.mahuta.buildtools.release.check.AbstractReleaseStepSpecification
/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@Subject(CommitNextDevelopmentVersionStep)
class CommitNextDevelopmentVersionStepTest extends AbstractReleaseStepSpecification {

    def "description returns non null"() {
        expect:
        CommitNextDevelopmentVersionStep.instance.description != null
    }

    def "apply issues the correct operations"() {
        when:
        CommitNextDevelopmentVersionStep.instance.apply(artifactRelease, infrastructure)
        then:
        1 * infrastructure.getVcs().getFlowConfig().getDevelopmentBranch() >> "develop"
        1 * infrastructure.getVcs().checkout("develop")
        1 * infrastructure.getVersionHandler().toNextDevelopmentVersion("1.0.0") >> "1.1.0"
        1 * infrastructure.getBuildToolAdapter().setVersion("1.1.0")
        1 * infrastructure.getVersionStorage().store("1.1.0")
        1 * infrastructure.getVcs().commitFiles(_)
        1 * infrastructure.getVcs().push()
    }
}
