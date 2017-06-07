package tane.mahuta.gradle.plugin.version

import spock.lang.Specification
import tane.mahuta.gradle.plugin.ServiceLoaderProjectServiceFactory
import tane.mahuta.gradle.plugin.version.storage.GradlePropertiesProjectVersionStorageFactory

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class ProjectVersionStorageFactoryTest extends Specification {

    def 'service loader finds default implementations'() {
        expect:
        ServiceLoaderProjectServiceFactory.getInstance(ProjectVersionStorageFactory).factories.collect {
            it.class
        } == [GradlePropertiesProjectVersionStorageFactory]
    }

}
