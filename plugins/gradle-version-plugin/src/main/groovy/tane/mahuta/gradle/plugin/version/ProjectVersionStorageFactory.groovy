package tane.mahuta.gradle.plugin.version

import tane.mahuta.buildtools.version.VersionStorage
import tane.mahuta.gradle.plugin.ProjectServiceFactory

/**
 * Interface for a factory for a {@link tane.mahuta.buildtools.version.VersionStorage}.
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
interface ProjectVersionStorageFactory extends ProjectServiceFactory<VersionStorage> {
}