package tane.mahuta.buildtools.dependency.simple

import spock.lang.Specification

/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
class DefaultDependencyContainerTest extends Specification {

    def 'builder works'() {
        setup:
        final name = 'a'
        final dependencies = [] as Set

        when:
        final actual = DefaultDependencyContainer.builder().name(name).dependencies(dependencies).build()
        then:
        actual.name.is(name)
        and:
        actual.dependencies.is(dependencies)
    }

}
