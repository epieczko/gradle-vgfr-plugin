package tane.mahuta.gradle.plugins.version.persistence.impl

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.build.version.VersionStorage
import tane.mahuta.gradle.plugins.ServiceLoaderProjectServiceFactory
import tane.mahuta.gradle.plugins.version.ProjectVersionStorageFactory
import tane.mahuta.gradle.plugins.version.storage.CompositeProjectVersionStorageFactory
import tane.mahuta.gradle.plugins.version.storage.GradlePropertiesProjectVersionStorageFactory
/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
@Subject(CompositeProjectVersionStorageFactory)
class CompositeProjectVersionStorageFactoryTest extends Specification {

    private static final List<Class<GradlePropertiesProjectVersionStorageFactory>> DEFAULT_FACTORY_CLASSES = [GradlePropertiesProjectVersionStorageFactory]

    private final project = ProjectBuilder.builder().build()

    def "factory service loader finds all factories"() {
        setup:
        final factory = ServiceLoaderProjectServiceFactory.getInstance(ProjectVersionStorageFactory)
        expect: 'all default factories have been found'
        factory.factories.collect{it.class}.containsAll(DEFAULT_FACTORY_CLASSES)
        and: 'the factory is initialized once only'
        ServiceLoaderProjectServiceFactory.getInstance(ProjectVersionStorageFactory).is(factory)
    }

    def "create invokes factories and creates storages"() {
        setup: 'factories and storages'
        final storages = [Mock(VersionStorage), Mock(VersionStorage)]
        final factories = [Mock(ProjectVersionStorageFactory), Mock(ProjectVersionStorageFactory), Mock(ProjectVersionStorageFactory)]
        final factory = new CompositeProjectVersionStorageFactory(factories)

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
        final factory = new CompositeProjectVersionStorageFactory([])
        expect:
        factory.create(project) == null
    }
}
