package tane.mahuta.buildtools.version;

import javax.annotation.Nullable;

/**
 * Interface for default transformation operations
 *
 * @author christian.heike@icloud.com
 *         Created on 11.06.17.
 */
public interface TransformationOperations<S extends TransformationOperations<S>> {

    /**
     * @return the release version representation of the current version
     */
    S toRelease();

    /**
     * Transform the version
     * @param l the change level
     * @return the next snapshot version
     */
    S toNextSnapshot(@Nullable ChangeLevel l);
}
