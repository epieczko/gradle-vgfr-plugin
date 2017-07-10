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
        final externalDependencies = [] as Set
        final internalDependencies = [] as Set

        when:
        final actual = DefaultDependencyContainer.builder()
                .name(name)
                .externalDependencies(externalDependencies)
                .internalDependencies(internalDependencies)
                .build()
        then:
        actual.name.is(name)
        actual.externalDependencies.is(externalDependencies)
        actual.internalDependencies.is(internalDependencies)
    }

}
