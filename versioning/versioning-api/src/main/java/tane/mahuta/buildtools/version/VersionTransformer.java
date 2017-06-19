package tane.mahuta.buildtools.version;

/**
 * Interface for a version transformer.
 *
 * @param <S> the source type
 * @param <T> the target type
 * @author christian.heike@icloud.com
 *         Created on 19.06.17.
 */
public interface VersionTransformer<S, T> {

    /**
     * Transforms a version.
     *
     * @param source the source version
     * @return the target version
     */
    T transform(S source);

}
