package tane.mahuta.buildtools.dependency;

import javax.annotation.Nonnull;

/**
 * A dependency container which is an {@link Iterable} of {@link ResolvedArtifact}s containing all (flattened) dependencies.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public interface DependencyContainer extends Iterable<GAVCDescriptor> {

    /**
     * @return the name of the container
     */
    @Nonnull
    String getName();

}
