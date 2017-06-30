package tane.mahuta.buildtools.dependency;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A dependency container which is an {@link Iterable} of {@link Artifact}s containing all (flattened) dependencies.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public interface DependencyContainer {

    /**
     * @return the name of the container
     */
    @Nonnull
    String getName();

    /**
     * @return the external (non-project) dependencies as {@link GAVCDescriptor}s
     */
    @Nonnull
    Set<? extends GAVCDescriptor> getExternalDependencies();

    /**
     * @return the internal (project) dependencies as {@link GAVCDescriptor}s
     */
    Set<? extends GAVCDescriptor> getInternalDependencies();
}
