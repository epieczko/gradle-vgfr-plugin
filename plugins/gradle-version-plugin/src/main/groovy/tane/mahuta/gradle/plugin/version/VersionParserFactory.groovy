package tane.mahuta.gradle.plugin.version

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionParser

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
     * Create a parser from the provided {@link Closure}.
     * @param parser the parser closure
     * @return a parser which calls the closure
     */
    @Nonnull
    static VersionParser<? extends Version> create(@Nonnull
                                                   @ClosureParams(value = FromString, options = "java.lang.Object")
                                                   final Closure<? extends Version> parser) {
        new VersionParser<Version>() {
            @Override
            Version parse(@Nullable Object source) {
                parser.call(source)
            }
        }
    }

}
