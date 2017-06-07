package tane.mahuta.gradle.plugin

import org.gradle.api.Project
import spock.lang.Specification
/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class CompositeProjectServiceFactoryTest extends Specification {

    private final Iterable<TestServiceFactory> factories = [Mock(TestServiceFactory), Mock(TestServiceFactory)]
    private final CompositeProjectServiceFactory factory = new CompositeProjectServiceFactory(factories)

    def "create provides null if no factory can be used"() {
        expect:
        factory.create(Mock(Project)) == null
    }

    def "create provides first non null factored service"() {
        setup:
        final project = Mock(Project)
        when:
        final factored = factory.create(project)
        then:
        1 * factories[0].create(project) >> null
        and:
        1 * factories[1].create(project) >> "X"
        and:
        factored == "X"
    }
}
