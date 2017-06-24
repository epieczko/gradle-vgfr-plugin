package tane.mahuta.buildtools.dependency.simple;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import tane.mahuta.buildtools.dependency.Artifact;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Default implementation of {@link Artifact}.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultArtifact extends AbstractArtifact {

    @Builder
    protected DefaultArtifact(@Nonnull final GAVCDescriptor descriptor, @Nullable final File localFile) {
        super(descriptor, localFile);
    }
}
