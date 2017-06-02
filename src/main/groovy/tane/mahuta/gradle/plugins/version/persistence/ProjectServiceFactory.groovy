package tane.mahuta.gradle.plugins.version.persistence

import org.gradle.api.Project

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Interface for a service factory using a {@link Project}.
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
interface ProjectServiceFactory<T> {

    @Nullable
    T create(@Nonnull final Project project)

}