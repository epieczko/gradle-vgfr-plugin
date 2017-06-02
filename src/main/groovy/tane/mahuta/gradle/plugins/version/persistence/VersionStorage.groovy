package tane.mahuta.gradle.plugins.version.persistence

import javax.annotation.Nullable

/**
 * Persistence handler, which stores versioning information.
 * @author christian.heike@icloud.com
 * Created on 23.05.17.
 */
interface VersionStorage {

    /**
     * Store the provided version for the project.
     * @param version the version to be stored
     */
    void store(@Nullable def version)

    /**
     * Load the version for the provided project.
     * @param project the project to load the version for
     * @return the version of the project
     */
    @Nullable
    def load()

}