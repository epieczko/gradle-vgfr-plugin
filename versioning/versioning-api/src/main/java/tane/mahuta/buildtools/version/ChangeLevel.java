package tane.mahuta.buildtools.version;

import lombok.Getter;

import javax.annotation.Nonnull;

/**
 * An enumeration describing the change level of project.
 *
 * @author christian.heike@icloud.com
 */
public enum ChangeLevel {

    MAJOR("The API was rendered incompatible"),
    MINOR("Extensions have been made to the API"),
    MICRO("Only implementations have been changed");

    @Getter(onMethod = @_(@Nonnull))
    private final String description;

    ChangeLevel(@Nonnull final String description) {
        this.description = description;
    }
    
}
