package tane.mahuta.gradle.plugin

import org.junit.Rule
import spock.lang.Specification

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class ServiceLoaderProjectServiceFactoryTest extends Specification {

    @Rule
    final ProjectBuilderTestRule projectBuilder = new ProjectBuilderTestRule()

    def 'loads implementations'() {
        when:
        final factory = ServiceLoaderProjectServiceFactory.getInstance(projectBuilder.project, TestServiceFactory)
        then:
        factory != null
        and:
        factory.create(projectBuilder.project) == projectBuilder.project.name
        and:
        ServiceLoaderProjectServiceFactory.getInstance(projectBuilder.project, TestServiceFactory).is(factory)
    }

}
