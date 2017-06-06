package tane.mahuta.gradle.plugins.version

import tane.mahuta.build.version.VersionStorage
import tane.mahuta.gradle.plugins.ProjectServiceFactory

/**
 * Interface for a factory for a {@link tane.mahuta.build.version.VersionStorage}.
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
interface ProjectVersionStorageFactory extends ProjectServiceFactory<VersionStorage> {
}