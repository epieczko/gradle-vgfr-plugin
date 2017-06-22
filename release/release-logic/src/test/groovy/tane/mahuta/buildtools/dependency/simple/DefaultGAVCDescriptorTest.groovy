package tane.mahuta.buildtools.dependency.simple

import spock.lang.Specification
import spock.lang.Subject

/**
 * @author christian.heike@icloud.com
 * Created on 22.06.17.
 */
@Subject(DefaultGAVCDescriptor)
class DefaultGAVCDescriptorTest extends Specification {

    def 'builder creates correct descriptor'() {
        when:
        final actual = DefaultGAVCDescriptor.builder().group("x").artifact("y").version("z").classifier("q").build()
        then:
        actual.group == 'x'
        and:
        actual.artifact == 'y'
        and:
        actual.version == 'z'
        and:
        actual.classifier == 'q'
    }

    def 'throws exception for nulls'() {
        when:
        DefaultGAVCDescriptor.builder().group(null).artifact('x').version('y').build()
        then:
        thrown(NullPointerException)
        when:
        DefaultGAVCDescriptor.builder().group('x').artifact(null).version('y').build()
        then:
        thrown(NullPointerException)
        when:
        DefaultGAVCDescriptor.builder().group('x').artifact('y').version(null).build()
        then:
        thrown(NullPointerException)

    }

}
