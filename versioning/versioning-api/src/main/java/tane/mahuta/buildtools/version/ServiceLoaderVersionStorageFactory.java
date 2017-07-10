package tane.mahuta.buildtools.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * An implementation of {@link VersionStorageFactory} using {@link ServiceLoader}.
 *
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
public class ServiceLoaderVersionStorageFactory implements VersionStorageFactory {

    private static final ThreadLocal<ServiceLoaderVersionStorageFactory> TL = ThreadLocal.withInitial(() -> new ServiceLoaderVersionStorageFactory());

    private final List<VersionStorageFactory> factories;

    private ServiceLoaderVersionStorageFactory() {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final ServiceLoader<VersionStorageFactory> serviceLoader = ServiceLoader.load(VersionStorageFactory.class, classLoader);
        factories = StreamSupport.stream(Spliterators.spliteratorUnknownSize(serviceLoader.iterator(), Spliterator.NONNULL), false).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public VersionStorage create(@Nonnull final File directory) {
        return factories.stream().map(f -> f.create(directory)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    /**
     * @return the thread dependent service instance
     */
    public static ServiceLoaderVersionStorageFactory getInstance() {
        return TL.get();
    }
}
