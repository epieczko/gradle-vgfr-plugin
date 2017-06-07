package tane.mahuta.gradle.plugin

import org.gradle.api.Project
import spock.lang.Specification
/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class ServiceLoaderProjectServiceFactoryTest extends Specification {

    private final Project project = Stub(Project) {
        getName() >> "bla"
    }

    def 'loads implementations'() {
        when:
        final factory = ServiceLoaderProjectServiceFactory.getInstance(TestServiceFactory)
        then:
        factory != null
        and:
        factory.create(project) == project.name
    }

}
