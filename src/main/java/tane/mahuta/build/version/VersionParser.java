package tane.mahuta.build.version;

import groovy.transform.CompileStatic;

import javax.annotation.Nullable;

/**
 * Interface for a parser for {@link Version}.
 *
 * @param <V> the concrete type of the parsed version
 *
 * @author christian.heike@icloud.com
 *         Created on 04.06.17.
 */
@CompileStatic
public interface VersionParser<V extends Version> {

    /**
     * Parse the provided source to a version.
     *
     * @param source the source to be parsed
     * @return the parsed version
     */
    @Nullable
    V parse(@Nullable Object source);
}
