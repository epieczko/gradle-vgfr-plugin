package tane.mahuta.build.version;

import javax.annotation.Nonnull;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
class SemanticVersionCache {

    private static ThreadLocal<SemanticVersionCache> TL = new InheritableThreadLocal<SemanticVersionCache>() {
        @Override
        protected SemanticVersionCache initialValue() {
            return new SemanticVersionCache();
        }
    };

    /**
     * A cache for the semantic versions
     */
    private Map<String, SoftReference<? extends SemanticVersion>> CACHE = new ConcurrentHashMap<>();

    private SemanticVersionCache() {
    }

    /**
     * @return the singleton instance
     */
    static SemanticVersionCache getInstance() {
        return TL.get();
    }

    /**
     * Puts the provided version into the cache.
     *
     * @param cacheKey the key for caching
     * @param factory  the factory for the element
     * @return the provided version
     */
    @Nonnull
    <I extends SemanticVersion, T extends I> I fromCacheOrCreate(@Nonnull final String cacheKey, @Nonnull final Class<I> cls, @Nonnull final Supplier<T> factory) {
        return Optional.ofNullable(CACHE.get(cacheKey))
                .map(ref -> {
                    final SemanticVersion r = ref.get();
                    return cls.isInstance(r) ? (I) r : null;
                })
                .orElseGet(() -> {
                    final T version = factory.get();
                    CACHE.put(cacheKey, new SoftReference<>(version));
                    return version;
                });
    }

}
