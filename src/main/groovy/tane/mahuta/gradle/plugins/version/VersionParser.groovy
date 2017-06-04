package tane.mahuta.gradle.plugins.version

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString

import javax.annotation.Nonnull
import javax.annotation.Nullable
/**
 * Interface for a version parser. Each parser
 *
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@CompileStatic
interface VersionParser<T> {

    /**
     * Parse the provided source to a version.
     * @param source the source to be parsed
     * @return the parsed version
     */
    @Nullable
    T parse(def source)

    /**
     * Factory methods for {@link VersionParser}.
     * @author christian.heike@icloud.com
     * Created on 04.06.17.
     */
    class Factory {

        private Factory() {}

        /**
         * A version parser which returns the source object.
         */
        static VersionParser<Object> NOP = new VersionParser<Object>() {
            @Override
            Object parse(final Object source) { source }
        }

        /**
         * Create a parser from a {@link Closure}.
         * @param c the closure
         * @return the parser
         */
        static <V> VersionParser<V> fromClosure(@Nonnull
                                                @ClosureParams(value = FromString, options = "java.lang.Object")
                                                final Closure<V> c) {
            new VersionParser<Object>() {
                @Override
                def parse(final Object source) {
                    c.call(source)
                }
            } as VersionParser<V>
        }
    }

}