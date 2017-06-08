package tane.mahuta.gradle.plugin.version.transform

import javax.annotation.Nonnull

/**
 * An enumeration describing the change level of project.
 * @author christian.heike@icloud.com
 */
enum ChangeLevel {

    API_INCOMPATIBILITY("The API was rendered incompatible (major)"),
    API_EXTENSION("Extensions have been made to the API (minor)"),
    IMPLEMENTATION_CHANGED("Only implementations have been changed (micro)")

    final String description

    ChangeLevel(@Nonnull final String description) {
        this.description = description
    }

}