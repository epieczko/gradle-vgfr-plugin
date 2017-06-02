package tane.mahuta.gradle.plugins.version.transform

import javax.annotation.Nonnull

/**
 * Interface for a version transformer
 * @author christian.heike@icloud.com
 */
interface VersionTransformer {

    /**
     * Transforms the version provided.
     * @param version
     * @return the transformed version
     */
    def transform(def version)

    /**
     * Abstract {@link VersionTransformer} handling {@code null} values.
     * @author christian.heike@icloud.com
     */
    abstract class AbstractVersionTransformer implements VersionTransformer {

        @Override
        final def transform(final Object version) {
            version ? doTransform(version) : null
        }

        /**
         * @see VersionTransformer#transform(java.lang.Object)
         */
        abstract def doTransform(@Nonnull Object version)
    }

}