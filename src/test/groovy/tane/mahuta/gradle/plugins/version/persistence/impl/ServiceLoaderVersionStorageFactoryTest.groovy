package tane.mahuta.gradle.plugins.version.persistence.impl

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import tane.mahuta.gradle.plugins.version.persistence.VersionStorage
import tane.mahuta.gradle.plugins.version.persistence.VersionStorageFactory
/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
class ServiceLoaderVersionStorageFactoryTest extends Specification {

    private final project = ProjectBuilder.builder().build()

    def "factory service loader finds all factories"() {
        setup:
        final factory = new ServiceLoaderVersionStorageFactory()
        expect:
        factory.@serviceLoader.iterator().collect{it.class} == [GradlePropertiesVersionStorageFactory]
    }

    def "create invokes factories and creates storages"() {
        setup: 'factories and storages'
        final storages = [Mock(VersionStorage), Mock(VersionStorage)]
        final factories = [Mock(VersionStorageFactory), Mock(VersionStorageFactory), Mock(VersionStorageFactory)]
        final factory = new ServiceLoaderVersionStorageFactory() {
            @Override protected List<VersionStorageFactory> getFactories() { factories }
        }

        when: 'invoking the create method'
        final actual = factory.create(project)
        then: 'the first factory is invoked, but returns null'
        1 * factories[0].create(project) >> null
        and: 'the second factory is invoked, returns storage without version'
        1 * factories[1].create(project) >> storages[0]
        and: 'the third factory is invoked'
        1 * factories[2].create(project) >> storages[1]
        and: 'and the factory returns a version'
        1 * storages[1].load() >> "My.Version"
        and: 'the storage is being used'
        actual == storages[1]
    }

    def "empty factory will not return any storage"() {
        setup:
        final factory = new ServiceLoaderVersionStorageFactory() {
            @Override protected List<VersionStorageFactory> getFactories() { [] }
        }
        expect:
        factory.create(project) == null
    }
}
