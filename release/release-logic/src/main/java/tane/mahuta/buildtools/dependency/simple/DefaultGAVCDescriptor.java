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

    @Getter(onMethod = @__({@Nullable, @Override}))
    private final String classifier;

    @Builder
    protected DefaultGAVCDescriptor(@Nonnull @NonNull final String group,
                                    @Nonnull @NonNull final String artifact,
                                    @Nonnull @NonNull final String version,
                                    @Nullable final String classifier) {
        this.group = group;
        this.artifact = artifact;
        this.version = version;
        this.classifier = StringUtils.isBlank(classifier) ? null : classifier;
    }

}
