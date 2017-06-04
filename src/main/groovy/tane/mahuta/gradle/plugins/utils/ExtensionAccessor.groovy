package tane.mahuta.gradle.plugins.utils

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import org.gradle.api.Project

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 03.06.17.
 */
@CompileStatic
class ExtensionAccessor<T> {

    private final String name
    private final Closure<T> factory

    ExtensionAccessor(@Nonnull final String name,
                      @Nonnull @DelegatesTo(Project)
                      @ClosureParams(value = FromString, options = "org.gradle.api.Project") final Closure<T> factory) {
        this.name = name
        this.factory = factory
    }

    @Nullable
    T get(final Project project) {
        project.extensions.findByName(name) as T
    }

    @Nullable
    T getOrCreate(@Nonnull final Project project) {
        get(project) ?: create(project)
    }

    @Nullable
    private T create(@Nonnull final Project project) {
        factory.delegate = project
        final T result = factory.call(project)
        if (result != null) {
            project.extensions.add(name, result)
        }
        factory.delegate = null
        result
    }
}
