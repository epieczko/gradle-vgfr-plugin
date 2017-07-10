package tane.mahuta.buildtools.release;

import javax.annotation.Nonnull;

/**
 * Adapter for interacting
 *
 * @author christian.heike@icloud.com
 *         Created on 29.06.17.
 */
public interface BuildToolAdapter {

    /**
     * Set the version for the project.
     * @param version the version to be set.
     */
    void setVersion(@Nonnull Object version);

    /**
     * @return the version for the project
     */
    @Nonnull
    Object getVersion();

    /**
     * Run a release build.
     * @return {@code true} if the build succeeded, {@code false} otherwise
     */
    boolean buildRelease();

}
