package tane.mahuta.buildtools.version;

import javax.annotation.Nonnull;

/**
 * An enumeration describing the change level of project.
 *
 * @author christian.heike@icloud.com
 */
public enum ChangeLevel {

    API_INCOMPATIBILITY("The API was rendered incompatible (major)"), API_EXTENSION("Extensions have been made to the API (minor)"), IMPLEMENTATION_CHANGED("Only implementations have been changed (micro)");

    private final String description;

    ChangeLevel(@Nonnull final String description) {
        this.description = description;
    }

    /**
     * @return the description for the change level
     */
    public final String getDescription() {
        return description;
    }

}
