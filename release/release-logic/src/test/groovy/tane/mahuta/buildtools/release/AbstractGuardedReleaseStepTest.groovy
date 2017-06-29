package tane.mahuta.buildtools.release

import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@Subject(AbstractGuardedReleaseStep)
class AbstractGuardedReleaseStepTest extends Specification {

    private final AbstractGuardedReleaseStep step = new AbstractGuardedReleaseStep() {

        @Override
        String getDescription() {
            return ""
        }

        @Override
        protected void doApply(
                @Nonnull ArtifactRelease release,
                @Nonnull ReleaseInfrastructure releaseInfrastructure, @Nonnull Object version) throws Exception {
            throw new IllegalArgumentException()
        }
    }

    def 'creates problems for exceptions'() {
        setup:
        final release = Mock(ArtifactRelease)
        final infrastructure = Mock(ReleaseInfrastructure) {
            getVersionHandler() >> Mock(VersionHandler) {
                toReleaseVersion(_) >> "X"
            }
        }

        when:
        step.apply(release, infrastructure)
        then:
        1 * release.describeProblem(_)
    }

}
