package tane.mahuta.buildtools.dependency;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface for an dependency resolver.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public interface ArtifactResolver {

    /**
     * Resolve the dependency for the provided descriptor.
     *
     * @param descriptor the descriptor
     * @return the {@link Artifact} or {@code null} if no dependency was resolved for the provided descriptor
     */
    @Nullable
    ArtifactWithClasspath resolveArtifact(@Nonnull GAVCDescriptor descriptor);

    /**
     * Resolves the previous release dependency for the provided descriptor.
     * The resolved artifact should contain the release version
     *
     * @param currentDescriptor the descriptor
     * @return the {@link Artifact} of the previous release version or {@code null} if no dependency was resolved
     */
    @Nullable
    ArtifactWithClasspath resolveLastReleaseArtifact(@Nonnull GAVCDescriptor currentDescriptor);

}
