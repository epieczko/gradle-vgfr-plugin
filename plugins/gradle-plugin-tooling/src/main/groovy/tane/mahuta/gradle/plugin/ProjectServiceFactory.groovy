package tane.mahuta.gradle.plugin

import org.gradle.api.Project

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Interface for a project based factory.
 *
 * @param <T> the type of the factored object
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
interface ProjectServiceFactory<T> {

    /**
     * Create the object for the project.
     *
     * @param project the project
     * @return the factored instance or null
     */
    @Nullable
    T create(@Nonnull Project project)

}