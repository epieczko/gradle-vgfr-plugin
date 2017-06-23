package tane.mahuta.buildtools.dependency.simple;

import lombok.*;
import lombok.experimental.Delegate;
import tane.mahuta.buildtools.dependency.DependencyContainer;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;

import javax.annotation.Nonnull;

/**
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
@Builder
@EqualsAndHashCode
@ToString
public class DefaultDependencyContainer implements DependencyContainer {

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final String name;

    @NonNull
    @Delegate
    private final Iterable<GAVCDescriptor> dependencies;

}
