package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.buildtools.version.ServiceLoaderVersionStorageFactory
import tane.mahuta.gradle.plugin.version.VersioningExtension

import javax.annotation.Nonnull
/**
 * Version plugin which provides mechanisms to load and store versions.
 * <p>
 *     Usage:
 *     <pre>
 *         apply plugin: 'tane.mahuta.gradle.version-plugin'
 *
 *         // Define a parser (will reparse the version)
 *         versioning.parser = { v -> (v as String).split(/./) }*
 *         // Set a new version (this parses the version)
 *         version = "1.2.3"
 *
 *     </pre>
 * </p>
 *
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
class VersionPlugin implements Plugin<ProjectInternal> {

    @Override
    void apply(@Nonnull final ProjectInternal target) {

        final versioningExtension = target.extensions.create("versioning", VersioningExtension, target)

        versioningExtension.storage = ServiceLoaderVersionStorageFactory.getInstance().create(target.projectDir)

        target.metaClass.setVersion = {v -> versioningExtension.setVersion(v) }
    }

}
