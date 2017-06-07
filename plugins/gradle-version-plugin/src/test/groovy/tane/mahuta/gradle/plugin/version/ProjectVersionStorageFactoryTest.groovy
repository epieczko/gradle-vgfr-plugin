package tane.mahuta.gradle.plugin.version

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import spock.lang.Specification
import tane.mahuta.gradle.plugin.ServiceLoaderProjectServiceFactory
import tane.mahuta.gradle.plugin.version.storage.GradlePropertiesProjectVersionStorageFactory

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class ProjectVersionStorageFactoryTest extends Specification {

    private final Project project = Stub(Project) {
        getExtensions() >> Mock(ExtensionContainer)
    }

    def 'service loader finds default implementations'() {
        expect:
        ServiceLoaderProjectServiceFactory.getInstance(project, ProjectVersionStorageFactory).factories.collect {
            it.class
        } == [GradlePropertiesProjectVersionStorageFactory]
    }

}
