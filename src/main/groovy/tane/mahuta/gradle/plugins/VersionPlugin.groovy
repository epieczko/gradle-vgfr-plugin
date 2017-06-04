package tane.mahuta.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.gradle.plugins.version.VersionExtension
import tane.mahuta.build.version.VersionStorage
import tane.mahuta.gradle.plugins.version.ProjectVersionStorageFactory

import javax.annotation.Nonnull

/**
 * Version plugin which provides mechanisms to load and store versions.
 * <p>
 *     Usage:
 *     <pre>
 *         apply plugin: 'tane.mahuta.gradle.version-plugin'
 *
 *         // Load the version (done by application already)
 *         version.load()
 *
 *         // Define a parser (will reparse the version)
 *         version.parser = { v -> (v as String).split(/./) }*
 *         // Define a named transformation
 *         version.toSnapshot = { v -> v + 'SNAPSHOT' }*
 *         // Set a new version (this parses the version)
 *         version = "1.2.3"
 *
 *         // Store the version
 *         version.store()
 *     </pre>
 * </p>
 * <p>
 *     By default, the {@link VersionStorage} provided by {@link ProjectVersionStorageFactory.ServiceLoaderVersionStorageFactory#get()} will be used.
 * </p>
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
class VersionPlugin implements Plugin<ProjectInternal> {

    @Override
    void apply(@Nonnull final ProjectInternal target) {
        final version = new VersionExtension()
        final storage = ProjectVersionStorageFactory.ServiceLoaderVersionStorageFactory.get().create(target)
        version.setStorage(storage)
        target.version = version
        target.metaClass.setVersion = version.&setRawVersion
    }

}
