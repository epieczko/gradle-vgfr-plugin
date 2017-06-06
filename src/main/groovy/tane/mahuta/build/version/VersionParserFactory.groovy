package tane.mahuta.build.version

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Factory for {@link VersionParser}s.
 *
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@CompileStatic
class VersionParserFactory {

    private VersionParserFactory() {}

    /**
     * Decorates the provided {@link VersionParser} by wrapping it, calling the target parser first and then the closure.
     * @param target the parser to be decorated (optional)
     * @param parser the closure which is being invoked after {@link VersionParser#parse(java.lang.Object)}
     * @return the new parser
     */
    @Nonnull
    static VersionParser<? extends Version> decorate(@Nullable final VersionParser<? extends Version> target,
                                                     @Nonnull
                                                     @ClosureParams(value = FromString, options = "java.lang.Object")
                                                     final Closure<? extends Version> parser) {
        new VersionParser<Version>() {
            @Override
            Version parse(@Nullable Object source) {
                parser.call(target != null ? target.parse(source) : source)
            }
        }
    }

    /**
     * Create a parser from the provided {@link Closure}.
     * @param parser the parser closure
     * @return a parser which calls the closure
     */
    @Nonnull
    static VersionParser<? extends Version> create(@Nonnull
                                                   @ClosureParams(value = FromString, options = "java.lang.Object")
                                                   final Closure<? extends Version> parser) {
        decorate(null, parser)
    }

}
