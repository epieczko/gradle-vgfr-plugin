package tane.mahuta.buildtools.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Interface for a version transformer
 *
 * @param <V> the type of the version to be transformed
 * @param <T> the target version type
 * @author christian.heike@icloud.com
 */
public interface VersionTransformer {

    /**
     * Transforms the version provided.
     *
     * @param version the source version
     * @param args    additional arguments to be used
     * @return the transformed version
     */
    @Nullable
    Object transform(@Nullable Object version, @Nonnull Object... args);

    /**
     * Abstract {@link VersionTransformer} handling {@code null} values.
     *
     * @author christian.heike@icloud.com
     */
    abstract class AbstractVersionTransformer implements VersionTransformer {

        @Override
        @Nullable
        public final Object transform(@Nullable final Object version, @Nonnull Object... args) {
            return Optional.ofNullable(version).map(v -> doTransform(v, args)).orElse(null);
        }

        /**
         * @param version the source version
         * @param args    additional arguments to be used
         * @return the transformed version
         * @see VersionTransformer#transform(Object, Object[])
         */
        protected abstract Object doTransform(@Nonnull Object version, @Nonnull Object... args);
    }
}
