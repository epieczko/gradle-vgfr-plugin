package tane.mahuta.buildtools.release.check

import spock.lang.Subject
import spock.lang.Unroll
import tane.mahuta.buildtools.dependency.DependencyContainer
import tane.mahuta.buildtools.dependency.GAVCDescriptor

/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
@Subject(ReferencesSnapshotDependenciesCheck)
class ReferencesSnapshotDependenciesCheckTest extends AbstractReleaseStepSpecification {

    @Unroll
    def '#configurations finds #expectedProblems snapshot dependency configurations'() {
        setup:
        stubConfigurations(configurations)

        when:
        ReferencesSnapshotDependenciesCheck.instance.apply(artifactRelease, infrastructure)
        then:
        artifactRelease.getProblems().size() == expectedProblems

        where:
        configurations                         | expectedProblems
        [:]                                    | 0
        ['compile': ['a:b:2.4']]               | 0
        ['compile': ['a:b:2.4-SNAPSHOT']]      | 1
        ['compile'     : ['a:b:2.4-SNAPSHOT'],
         'otherCompile': ['a:c:2.3']]          | 1
        ['compile'     : ['a:b:2.4-SNAPSHOT'],
         'otherCompile': ['a:c:2.3-SNAPSHOT']] | 2
    }

    void stubConfigurations(final Map<String, List<String>> configurations) {
        artifactRelease.getDependencyContainers() >> (configurations.collect { name, deps ->
            final convertedDeps = deps.collect { dep ->
                final parts = dep.split(":")
                Mock(GAVCDescriptor) {
                    getGroup() >> parts[0]
                    getArtifact() >> parts[1]
                    getVersion() >> parts[2]
                    isSnapshot() >> { parts[2].endsWith("-SNAPSHOT") }
                }
            }
            Mock(DependencyContainer) {
                getName() >> name
                getDependencies() >> convertedDeps
            }
        } as Set)
    }
}
