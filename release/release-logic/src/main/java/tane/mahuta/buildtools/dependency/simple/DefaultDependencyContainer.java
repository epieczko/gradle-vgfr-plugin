package tane.mahuta.buildtools.dependency.simple;

import lombok.*;
import tane.mahuta.buildtools.dependency.DependencyContainer;
import tane.mahuta.buildtools.dependency.GAVCDescriptor;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Default implementation for {@link DependencyContainer}.
 *
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
@Builder
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultDependencyContainer implements DependencyContainer {

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final String name;

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final Set<? extends GAVCDescriptor> externalDependencies;

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final Set<? extends GAVCDescriptor> internalDependencies;

}
