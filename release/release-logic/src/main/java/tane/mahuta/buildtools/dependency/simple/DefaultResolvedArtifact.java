package tane.mahuta.buildtools.dependency.simple;

import lombok.Builder;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;
import tane.mahuta.buildtools.dependency.ResolvedArtifact;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Default implementation of {@link ResolvedArtifact}.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public class DefaultResolvedArtifact extends AbstractResolvedArtifact {

    @Builder
    public DefaultResolvedArtifact(@Nonnull final GAVCDescriptor descriptor, @Nullable final File localFile) {
        super(descriptor, localFile);
    }
}
