package tane.mahuta.buildtools.release;

import lombok.*;
import tane.mahuta.buildtools.dependency.Artifact;
import tane.mahuta.buildtools.dependency.DependencyContainer;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Set;

/**
 * Default implementation of {@link ArtifactRelease}.
 *
 * @author christian.heike@icloud.com
 *         Created on 24.06.17.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DefaultArtifactRelease extends AbstractArtifactRelease {

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final GAVCDescriptor descriptor;

    @Getter(onMethod = @__({@Nullable, @Override}))
    private final File localFile;

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final File projectDir;

    @NonNull
    @Getter(onMethod = @__({@Nonnull, @Override}))
    private final Set<? extends Artifact> classpathDependencies;

    @NonNull
    @Getter(onMethod = @__({@Nonnull, @Override}))
    private final Set<? extends DependencyContainer> dependencyContainers;

    @Getter(onMethod = @__(@Override))
    private final boolean internalArtifact;
}
