package tane.mahuta.buildtools.dependency.simple;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
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
@Builder
public class DefaultResolvedArtifactWithDependencies implements ResolvedArtifactWithDependencies {

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final GAVCDescriptor descriptor;

    @Getter(onMethod = @__({@Nullable, @Override}))
    private final File localFile;

    @Getter(onMethod = @__({@Nonnull, @Override}))
    private final Set<ResolvedArtifact> classpathDependencies;

}

