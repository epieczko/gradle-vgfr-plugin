package tane.mahuta.buildtools.dependency.simple

import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.dependency.GAVCDescriptor

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@Subject(DefaultResolvedArtifact)
class DefaultResolvedArtifactTest extends Specification {

    def 'builder builds correctly'() {
        setup:
        final descriptorMock = Mock(GAVCDescriptor)
        final file = new File('.')
        when:
        final actual = DefaultResolvedArtifact.builder().descriptor(descriptorMock).localFile(file).build()
        then:
        actual.descriptor.is(descriptorMock)
        and:
        actual.localFile.is(file)
    }

    def 'null throws exception'() {
        when:
        DefaultResolvedArtifact.builder().descriptor(null).build()
        then:
        thrown(NullPointerException)
    }

}
