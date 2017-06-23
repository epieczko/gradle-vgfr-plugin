package tane.mahuta.buildtools.dependency.simple;

import lombok.*;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;
import tane.mahuta.buildtools.dependency.ResolvedArtifact;
import tane.mahuta.buildtools.dependency.ResolvedArtifactWithDependencies;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Set;

/**
 * Default implementation of {@link ResolvedArtifactWithDependencies}.
 *
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultResolvedArtifactWithDependencies extends AbstractResolvedArtifact implements ResolvedArtifactWithDependencies {

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final Set<ResolvedArtifact> classpathDependencies;

    @Builder
    protected DefaultResolvedArtifactWithDependencies(@Nonnull final GAVCDescriptor descriptor,
                                                      @Nullable final File localFile,
                                                      @Nonnull final Set<ResolvedArtifact> classpathDependencies) {
        super(descriptor, localFile);
        this.classpathDependencies = classpathDependencies;
    }
}

