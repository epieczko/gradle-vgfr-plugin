package tane.mahuta.buildtools.dependency.simple;

import lombok.*;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Default implementation of {@link GAVCDescriptor}.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultGAVCDescriptor implements GAVCDescriptor {

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final String group;

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final String artifact;

    @Getter(onMethod = @__({@Nonnull, @Override}))
    @NonNull
    private final String version;

    @Getter(onMethod = @__({@Nullable, @Override}))
    private final String classifier;

}
