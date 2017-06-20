package tane.mahuta.gradle.plugin.version;

import org.gradle.api.Project;

import javax.annotation.Nonnull;

/**
 * Helper which provides access to getter and setter for the version on a {@link Project}.
 *
 * @author christian.heike@icloud.com
 *         Created on 16.06.17.
 */
public class ProjectVersionAccessor {

    private final Project project;

    ProjectVersionAccessor(@Nonnull final Project project) {
        this.project = project;
    }

    /**
     * @return the version value
     */
    public Object get() {
        return project.getVersion();
    }

    /**
     * Set the version value.
     * @param version the new value
     */
    public void set(final Object version) {
        project.setVersion(version);
    }

}
