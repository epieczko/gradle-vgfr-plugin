package tane.mahuta.buildtools.version.storage

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.buildtools.version.VersionStorageFactory

/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
@Subject(GradlePropertiesProjectVersionStorageFactory)
class GradlePropertiesProjectVersionStorageFactoryTest extends Specification {

    @Rule
    final TemporaryFolder folder = new TemporaryFolder()

    private final storageFactory = new GradlePropertiesProjectVersionStorageFactory()

    def "propertyVersionFileIn returns gradle.properties"() {
        expect:
        storageFactory.propertyVersionFileIn(folder.getRoot()) == new File(folder.getRoot(), "gradle.storage")
    }

    def "propertyName returns version"() {
        expect:
        storageFactory.propertyName() == "version"
    }

    def 'service loader finds implementation'() {
        expect:
        ServiceLoader.load(VersionStorageFactory).iterator().collect {
            it.class
        }.contains(GradlePropertiesProjectVersionStorageFactory)
    }
}
