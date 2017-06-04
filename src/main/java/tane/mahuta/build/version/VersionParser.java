package tane.mahuta.build.version;

import groovy.transform.CompileStatic;

import javax.annotation.Nullable;

/**
 * Interface for a version parser. Each parser
 *
 * @author christian.heike@icloud.com
 *         Created on 04.06.17.
 */
@CompileStatic
public interface VersionParser<T> {

    /**
     * Parse the provided source to a version.
     *
     * @param source the source to be parsed
     * @return the parsed version
     */
    @Nullable
    T parse(@Nullable Object source);

}
