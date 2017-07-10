package tane.mahuta.buildtools.dependency.simple;

import lombok.*;
import tane.mahuta.buildtools.dependency.Artifact;
import tane.mahuta.buildtools.dependency.ArtifactWithClasspath;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Set;

/**
 * Default implementation of {@link ArtifactWithClasspath}.
 *
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultArtifactWithClasspath extends AbstractArtifact implements ArtifactWithClasspath {

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final Set<? extends Artifact> classpathDependencies;

    @Builder
    protected DefaultArtifactWithClasspath(@Nonnull final GAVCDescriptor descriptor,
                                           @Nullable final File localFile,
                                           @Nonnull final Set<? extends Artifact> classpathDependencies,
                                           final boolean internalArtifact) {
        super(descriptor, localFile, internalArtifact);
        this.classpathDependencies = classpathDependencies;
    }
}

