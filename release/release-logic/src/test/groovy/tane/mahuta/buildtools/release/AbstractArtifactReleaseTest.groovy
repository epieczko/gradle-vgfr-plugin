package tane.mahuta.buildtools.release

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.dependency.DependencyContainer
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.Artifact
import tane.mahuta.buildtools.release.reporting.Severity

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@Subject(AbstractArtifactRelease)
class AbstractArtifactReleaseTest extends Specification {

    @Rule
    final TemporaryFolder folder = new TemporaryFolder()

    private AbstractArtifactRelease release

    def setup() {
        final descriptorMock = Mock(GAVCDescriptor)
        release = new AbstractArtifactRelease() {
            final Set<DependencyContainer> dependencyContainers = [] as Set
            final File projectDir = folder.root
            final Set<Artifact> classpathDependencies = [] as Set
            final GAVCDescriptor descriptor = descriptorMock
            final File localFile = new File(folder.root, "my.jar")
        }
    }

    def "problems can be described"() {
        when:
        release.describeProblem({ b -> b.severity(Severity.PROBLEM).messageFormat("x").formatArgs() })
        then:
        release.problems.size() == 1
    }
}
