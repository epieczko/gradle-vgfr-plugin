package tane.mahuta.gradle.plugin.version

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import tane.mahuta.buildtools.version.VersionTransformer

import javax.annotation.Nonnull
import javax.annotation.Nullable
/**
 * Factory for {@link VersionTransformer}s.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
class VersionTransformerFactory {

    private VersionTransformerFactory() {}

    @Nullable
    static VersionTransformer create(
            @ClosureParams(value = FromString, options = ["java.lang.Object", "java.lang.Object[]"])
            @Nonnull final Closure<?> transformer) {

        final paramTypes = transformer.getParameterTypes()
        if (paramTypes.length == 0) {
            return null
        }

        new VersionTransformer() {
            @Override
            Object transform(@Nullable final Object version, @Nonnull final Object... args) {
                paramTypes.length == 1 ? transformer.call(version) : transformer.call(version, args)
            }
        }
    }

}
