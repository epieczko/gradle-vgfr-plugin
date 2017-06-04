package tane.mahuta.gradle.plugins.version.persistence

import org.gradle.api.Project
import tane.mahuta.gradle.plugins.version.persistence.impl.CompositeVersionStorageFactory

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Interface for a factory for a {@link VersionStorage}.
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
interface VersionStorageFactory {

    /**
     * Create a new {@link VersionStorage} for the provided project
     * @param project the project
     * @return the version storage
     */
    @Nullable
    VersionStorage create(@Nonnull final Project project)

    /**
     * {@link ServiceLoader} backed {@link VersionStorageFactory}.
     * @author christian.heike@icloud.com
     * Created on 02.06.17.
     */
    final class ServiceLoaderVersionStorageFactory {

        private static final ThreadLocal<CompositeVersionStorageFactory> SL_INSTANCE_TL = new InheritableThreadLocal<CompositeVersionStorageFactory>() {
            @Override
            protected CompositeVersionStorageFactory initialValue() {
                new CompositeVersionStorageFactory(ServiceLoader.load(VersionStorageFactory).iterator().collect {
                    it as VersionStorageFactory
                })
            }
        }

        private ServiceLoaderVersionStorageFactory() {}

        /**
         * @return a {@link ThreadLocal} backed implementation
         */
        static VersionStorageFactory get() {
            SL_INSTANCE_TL.get()
        }

    }

}