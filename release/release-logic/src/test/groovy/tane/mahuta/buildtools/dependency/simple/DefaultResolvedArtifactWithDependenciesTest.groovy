package tane.mahuta.buildtools.dependency.simple

import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.dependency.GAVCDescriptor

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@Subject(DefaultArtifactWithClasspath)
class DefaultResolvedArtifactWithDependenciesTest extends Specification {

    def 'builder builds correctly'() {
        setup:
        final descriptorMock = Mock(GAVCDescriptor)
        final file = new File('.')
        final dependencies = [] as Set
        when:
        final actual = DefaultArtifactWithClasspath.builder()
                .descriptor(descriptorMock)
                .localFile(file)
                .classpathDependencies(dependencies)
                .build()

        then:
        actual.descriptor.is(descriptorMock)
        and:
        actual.localFile.is(file)
        and:
        actual.classpathDependencies.is(dependencies)
    }

    def 'null throws exception'() {
        when:
        DefaultArtifactWithClasspath.builder().descriptor(null).classpathDependencies([]).build()
        then:
        thrown(IllegalArgumentException)
    }

}
