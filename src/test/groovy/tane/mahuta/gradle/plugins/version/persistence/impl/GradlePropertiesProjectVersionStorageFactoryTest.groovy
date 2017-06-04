package tane.mahuta.gradle.plugins.version.persistence.impl

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.gradle.plugins.version.storage.GradlePropertiesProjectVersionStorageFactory

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
        storageFactory.propertyVersionFileIn(folder.getRoot()) == new File(folder.getRoot(), "gradle.properties")
    }

    def "propertyName returns version"() {
        expect:
        storageFactory.propertyName() == "version"
    }
}
