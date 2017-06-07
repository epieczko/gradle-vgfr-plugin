package tane.mahuta.gradle.plugin

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.Project

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Composite implementation {@link ProjectServiceFactory}.
 *
 * <p>
 *     Delegates {@link ProjectServiceFactory#create(org.gradle.api.Project)} to each
 *     provided {@link CompositeProjectServiceFactory#factories} and returns the first non
 *     null instance.
 * </p>
 * <p>
 *     Also caches the factored service.
 * </p>
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@CompileStatic
@Slf4j
class CompositeProjectServiceFactory<T> implements ProjectServiceFactory<T> {

    private final Map<String, T> factoredCache = [:]
    protected final Iterable<ProjectServiceFactory<T>> factories

    CompositeProjectServiceFactory(@Nonnull final Iterable<ProjectServiceFactory<T>> factories) {
        this.factories = factories
    }

    @Override
    @Nullable
    T create(@Nonnull final Project project) {
        final result = factoredCache[project.path] ?: doCreate(project)
        factoredCache[project.path] = result
        result
    }

    @Nullable
    private T doCreate(@Nonnull final Project project) {
        for (final factory in factories) {
            final factored = factory.create(project)
            if (factored != null) {
                log.debug("Created factored {} using: {}", factored, factory)
                return factored
            }
        }
        log.debug("Could not factor service, tried: {}", factories)
        return null
    }

}
