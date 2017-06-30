package tane.mahuta.buildtools.dependency.simple

import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.dependency.GAVCDescriptor

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@Subject(DefaultArtifact)
class DefaultResolvedArtifactTest extends Specification {

    def 'builder builds correctly'() {
        setup:
        final descriptorMock = Mock(GAVCDescriptor)
        final file = new File('.')
        when:
        final actual = DefaultArtifact.builder().descriptor(descriptorMock).localFile(file).internalArtifact(true).build()
        then:
        actual.descriptor.is(descriptorMock)
        actual.localFile.is(file)
        actual.internalArtifact == true
    }

    def 'null throws exception'() {
        when:
        DefaultArtifact.builder().descriptor(null).build()
        then:
        thrown(RuntimeException)
    }

}
