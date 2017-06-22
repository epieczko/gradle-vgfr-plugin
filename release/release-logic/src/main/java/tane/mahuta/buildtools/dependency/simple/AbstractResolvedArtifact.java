package tane.mahuta.buildtools.dependency.simple;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;
import tane.mahuta.buildtools.dependency.ResolvedArtifact;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Abstract implementation of {@link ResolvedArtifact}.
 *
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
@RequiredArgsConstructor
public abstract class AbstractResolvedArtifact implements ResolvedArtifact {

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final GAVCDescriptor descriptor;

    @Getter(onMethod = @__({@Nullable, @Override}))
    private final File localFile;

}
