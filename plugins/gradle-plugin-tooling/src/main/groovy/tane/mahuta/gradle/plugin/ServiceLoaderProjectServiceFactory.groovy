package tane.mahuta.gradle.plugin

import groovy.transform.CompileStatic
import org.gradle.api.Project

/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@CompileStatic
class ServiceLoaderProjectServiceFactory<T> extends CompositeProjectServiceFactory<T> {

    public static final String EXTENSION_NAME = "serviceFactoryCache"

    private ServiceLoaderProjectServiceFactory(
            final Class<? extends ProjectServiceFactory<T>> factoryInterface) {
        super(ServiceLoader.load(factoryInterface))
    }

    static <T> ProjectServiceFactory<T> getInstance(
            final Project project, final Class<? extends ProjectServiceFactory<T>> factoryInterface) {

        synchronized (project) {
            final cache = ((project.extensions.findByName(EXTENSION_NAME) ?:
                    createExtension(project)) as Map<Class<? extends ProjectServiceFactory<?>>, ProjectServiceFactory<?>>)

            def result = cache.get(factoryInterface)
            if (result == null) {
                cache.put(factoryInterface, result = new ServiceLoaderProjectServiceFactory<T>(factoryInterface))
            }
            return result as ProjectServiceFactory<T>
        }

    }

    private static def createExtension(Project project) {
        def result = [:]
        project.extensions.add(EXTENSION_NAME, result)
        result
    }
}
