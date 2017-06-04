package tane.mahuta.gradle.plugins.utils

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import org.gradle.api.Project

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * A helper for easily accessing project extensions
 * @author christian.heike@icloud.com
 * Created on 03.06.17.
 */
@CompileStatic
class ProjectExtensionHelper<T> {

    private final String name
    private final Closure<T> factory

    /**
     * Create a new accessor with a name and factory method.
     * @param name the name of the extension point
     * @param factory
     */
    ProjectExtensionHelper(@Nonnull final String name,
                           @Nonnull @DelegatesTo(Project)
                     @ClosureParams(value = FromString, options = "org.gradle.api.Project") final Closure<T> factory) {
        this.name = name
        this.factory = factory
    }

    /**
     * Uses {@link Project#getExtensions()} and {@link org.gradle.api.plugins.ExtensionContainer#findByName(java.lang.String)}
     * to look up the extension.
     * @param project the project to lookup the extension
     * @return the extension, or {@code null} if none is found
     */
    @Nullable
    T get(final Project project) {
        project.extensions.findByName(name) as T
    }

    /**
     * Uses {@link Project#getExtensions()} and {@link org.gradle.api.plugins.ExtensionContainer#findByName(java.lang.String)}
     * to look up the extension, if no extension is present, will create and use
     * {@link org.gradle.api.plugins.ExtensionContainer#add(java.lang.String, java.lang.Object)} to add it.
     * @param project th eproject
     * @return the extension, or {@code null} if the factory method returns null
     */
    @Nullable
    T getOrCreate(@Nonnull final Project project) {
        get(project) ?: createAndAdd(project)
    }

    @Nullable
    private T createAndAdd(@Nonnull final Project project) {
        factory.delegate = project
        final T result = factory.call(project)
        if (result != null) {
            project.extensions.add(name, result)
        }
        factory.delegate = null
        result
    }
}
