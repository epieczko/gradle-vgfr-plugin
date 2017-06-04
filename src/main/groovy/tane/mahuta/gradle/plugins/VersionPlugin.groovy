package tane.mahuta.gradle.plugins

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.gradle.plugins.utils.ExtensionAccessor
import tane.mahuta.gradle.plugins.version.persistence.VersionStorage
import tane.mahuta.gradle.plugins.version.persistence.VersionStorageFactory

import javax.annotation.Nonnull
/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
@CompileStatic
class VersionPlugin implements Plugin<ProjectInternal> {

    static final ExtensionAccessor<VersionStorage> CONVENTION =
            new ExtensionAccessor<>("versionStorage", VersionStorageFactory.ServiceLoaderVersionStorageFactory.get().&create)


    @Override
    void apply(@Nonnull final ProjectInternal target) {
        target.version = CONVENTION.getOrCreate(target)?.load()
    }

}
