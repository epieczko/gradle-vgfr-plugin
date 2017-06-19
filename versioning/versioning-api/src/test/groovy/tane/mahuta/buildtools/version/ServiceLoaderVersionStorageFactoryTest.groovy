package tane.mahuta.buildtools.version

import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 19.06.17.
 */
@Subject(ServiceLoaderVersionStorageFactory)
class ServiceLoaderVersionStorageFactoryTest extends Specification {

    private static ThreadLocal<VersionStorageFactory> MOCK_TL = new ThreadLocal<VersionStorageFactory>();

    def 'finds defined services and invokes it'() {
        setup:
        final factoryMock = Mock(VersionStorageFactory)
        final mock = Mock(VersionStorage)
        final f = new File(".")
        MOCK_TL.set(factoryMock)

        when:
        final actual = factoryMock.create(f)
        then:
        1 * factoryMock.create(f) >> mock
        and:
        actual.is(mock)
    }

    static class TestVersionStorageFactory implements VersionStorageFactory {

        @Override
        VersionStorage create(@Nonnull File directory) {
            return MOCK_TL.get().create(directory)
        }
    }

}
