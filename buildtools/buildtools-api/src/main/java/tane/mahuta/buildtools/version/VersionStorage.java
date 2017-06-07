package tane.mahuta.buildtools.version;

import javax.annotation.Nullable;

/**
 * Persistence handler which stores versioning information.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.05.17.
 */
public interface VersionStorage {
    /**
     * Store the provided version for the project.
     *
     * @param version the version to be stored
     */
    void store(@Nullable Object version);

    /**
     * Load the version for the provided project.
     *
     * @return the version of the project
     */
    @Nullable
    Object load();
}
