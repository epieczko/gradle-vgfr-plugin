package tane.mahuta.gradle.plugins.version.persistence.impl

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
class GradlePropertiesVersionStorageFactoryTest extends Specification {

    @Rule
    final TemporaryFolder folder = new TemporaryFolder()
    private final storageFactory = new GradlePropertiesVersionStorageFactory()

    def "propertyVersionFileIn returns gradle.properties"() {
        expect:
        storageFactory.propertyVersionFileIn(folder.getRoot()) == new File(folder.getRoot(), "gradle.properties")
    }

    def "propertyName returns version"() {
        expect:
        storageFactory.propertyName() == "version"
    }
}
