package tane.mahuta.gradle.plugins

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.gradle.plugins.utils.ProjectExtensionHelper
import tane.mahuta.gradle.plugins.version.persistence.VersionStorage
import tane.mahuta.gradle.plugins.version.persistence.VersionStorageFactory

import javax.annotation.Nonnull

/**
 * Version plugin which provides mechanisms to load and store versions.
 * <p>
 *     Usage:
 *     <pre>
 *         apply plugin: 'tane.mahuta.gradle.version-plugin'
 *     </pre>
 * </p>
 * <p>
 *     It uses {@link VersionStorage} provided by
 * {@link tane.mahuta.gradle.plugins.version.persistence.VersionStorageFactory.ServiceLoaderVersionStorageFactory#get()}.
 * </p>
 * <p>
 *     The version is loaded upon application and can be loaded and stored by
 *     <pre>
 *         versionStorage.load()
 *         versionStorage.save(newVersion)
 *     </pre>
 * </p>
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
@CompileStatic
class VersionPlugin implements Plugin<ProjectInternal> {

    static final ProjectExtensionHelper<VersionStorage> CONVENTION =
            new ProjectExtensionHelper<>("versionStorage", VersionStorageFactory.ServiceLoaderVersionStorageFactory.get().&create)

    @Override
    void apply(@Nonnull final ProjectInternal target) {
        target.version = CONVENTION.getOrCreate(target)?.load()
    }

}
