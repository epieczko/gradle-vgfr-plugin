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
public interface VersionTransformer<V extends Version, T extends Version> {

    /**
     * Transforms the version provided.
     *
     * @param version the source version
     * @return the transformed version
     */
    @Nullable
    T transform(@Nullable V version);

    /**
     * Abstract {@link VersionTransformer} handling {@code null} values.
     *
     * @author christian.heike@icloud.com
     */
    abstract class AbstractVersionTransformer<V extends Version, T extends Version> implements VersionTransformer<V, T> {

        @Override
        @Nullable
        public final T transform(@Nullable final V version) {
            return Optional.ofNullable(version).map(this::doTransform).orElse(null);
        }

        /**
         * @see VersionTransformer#transform(Version)
         * @param version the source version
         * @return the transformed version
         */
        protected abstract T doTransform(@Nonnull V version);
    }
}
