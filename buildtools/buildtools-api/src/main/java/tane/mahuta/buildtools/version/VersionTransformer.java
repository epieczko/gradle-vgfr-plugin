package tane.mahuta.buildtools.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Interface for a version transformer
 *
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
     * Decorate the provided transformer, wrapping the transformation method into the transformation method of the provided decorator.
     *
     * @param decorator the decorator
     * @return the decorated transformer
     */
    default VersionTransformer decorate(@Nonnull final VersionTransformer decorator) {
        return (version, args) -> Optional.ofNullable(transform(version)).map(v -> decorator.transform(v, args)).orElse(null);
    }

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
