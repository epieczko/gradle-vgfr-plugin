package tane.mahuta.buildtools.dependency.simple;

import lombok.*;
import org.apache.commons.lang.StringUtils;
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
@EqualsAndHashCode
@ToString
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

    private final String classifier;

    @Override
    @Nullable
    public String getClassifier() {
        return StringUtils.isBlank(classifier) ? null : classifier;
    }
}
