package tane.mahuta.gradle.plugin

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@CompileStatic
class ServiceLoaderProjectServiceFactory<T> extends CompositeProjectServiceFactory<T> {

    private static
    final ThreadLocal<Map<Class<?>, ProjectServiceFactory<?>>> FACTORY_CACHE = new InheritableThreadLocal<Map<Class<?>, ProjectServiceFactory<?>>>() {
        @Override
        protected Map<Class<?>, ProjectServiceFactory<?>> initialValue() { [:] }
    }

    private ServiceLoaderProjectServiceFactory(
            final Class<? extends ProjectServiceFactory<T>> factoryInterface) {
        super(ServiceLoader.load(factoryInterface))
    }

    @PackageScope
    static void clearCache() {
        FACTORY_CACHE.get().clear()
    }

    static <T> ProjectServiceFactory<T> getInstance(final Class<? extends ProjectServiceFactory<T>> factoryInterface) {
        def cache = FACTORY_CACHE.get()
        synchronized (cache) {
            def result = cache.get(factoryInterface)
            if (result == null) {
                cache.put(factoryInterface, result = new ServiceLoaderProjectServiceFactory<T>(factoryInterface))
            }
            return result as ProjectServiceFactory<T>
        }
    }

}
