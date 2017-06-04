package tane.mahuta.gradle.plugins.version

import org.gradle.api.Project
import tane.mahuta.build.version.VersionStorage
import tane.mahuta.gradle.plugins.version.storage.CompositeProjectVersionStorageFactory

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Interface for a factory for a {@link tane.mahuta.build.version.VersionStorage}.
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
interface ProjectVersionStorageFactory {

    /**
     * Create a new {@link tane.mahuta.build.version.VersionStorage} for the provided project
     * @param project the project
     * @return the version storage
     */
    @Nullable
    VersionStorage create(@Nonnull final Project project)

    /**
     * {@link ServiceLoader} backed {@link ProjectVersionStorageFactory}.
     * @author christian.heike@icloud.com
     * Created on 02.06.17.
     */
    final class ServiceLoaderVersionStorageFactory {

        private static final ThreadLocal<CompositeProjectVersionStorageFactory> SL_INSTANCE_TL = new InheritableThreadLocal<CompositeProjectVersionStorageFactory>() {
            @Override
            protected CompositeProjectVersionStorageFactory initialValue() {
                new CompositeProjectVersionStorageFactory(ServiceLoader.load(ProjectVersionStorageFactory).iterator().collect {
                    it as ProjectVersionStorageFactory
                })
            }
        }

        private ServiceLoaderVersionStorageFactory() {}

        /**
         * @return a {@link ThreadLocal} backed implementation
         */
        static ProjectVersionStorageFactory get() {
            SL_INSTANCE_TL.get()
        }

    }

}