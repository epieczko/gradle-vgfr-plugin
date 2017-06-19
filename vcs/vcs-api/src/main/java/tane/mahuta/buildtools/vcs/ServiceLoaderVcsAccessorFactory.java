package tane.mahuta.buildtools.vcs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * {@link java.util.ServiceLoader} based {@link VcsAccessorFactory} which will check each implementation provided
 * by the service loader and returns the first factored instance.
 *
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
public class ServiceLoaderVcsAccessorFactory implements VcsAccessorFactory {

    private static final ThreadLocal<ServiceLoaderVcsAccessorFactory> TL = ThreadLocal.withInitial(() -> new ServiceLoaderVcsAccessorFactory(Thread.currentThread().getContextClassLoader()));

    private final List<VcsAccessorFactory> factories;

    private ServiceLoaderVcsAccessorFactory(@Nonnull final ClassLoader contextClassLoader) {
        final ServiceLoader<VcsAccessorFactory> serviceLoader = ServiceLoader.load(VcsAccessorFactory.class, contextClassLoader);
        factories = StreamSupport.stream(Spliterators.spliteratorUnknownSize(serviceLoader.iterator(), Spliterator.NONNULL), false).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public VcsAccessor create(@Nonnull final File directory) {
        return factories.stream().map(f -> f.create(directory)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    /**
     * @return the thread dependent service instance
     */
    public static ServiceLoaderVcsAccessorFactory getInstance() {
        return TL.get();
    }
}
