package tane.mahuta.build.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Interface for a version transformer
 *
 * @author christian.heike@icloud.com
 */
public interface VersionTransformer<T> {
    /**
     * Transforms the version provided.
     *
     * @param version
     * @return the transformed version
     */
    @Nullable
    T transform(@Nullable T version);

    /**
     * Abstract {@link VersionTransformer} handling {@code null} values.
     *
     * @author christian.heike@icloud.com
     */
    abstract class AbstractVersionTransformer<T> implements VersionTransformer<T> {

        @Override
        @Nullable
        public final T transform(@Nullable final T version) {
            return Optional.ofNullable(version).map(this::doTransform).orElse(null);
        }

        /**
         * @see VersionTransformer#transform(Object)
         */
        protected abstract T doTransform(@Nonnull T version);
    }
}
