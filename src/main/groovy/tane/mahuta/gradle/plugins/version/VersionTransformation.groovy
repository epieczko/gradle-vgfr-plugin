package tane.mahuta.gradle.plugins.version

import groovy.transform.CompileStatic
import tane.mahuta.gradle.plugins.version.transform.VersionTransformer

import javax.annotation.Nonnull

/**
 * Extension which aggregates all version transformers.
 *
 * <p>
 *  Usage:
 *  <pre>
 *      versionTransformation.on
 *  </pre>
 * </p>
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@CompileStatic
class VersionTransformation {

    @Delegate
    private final Map<String, VersionTransformer> transformerMap = [:]

    def methodMissing(final String name, final args) {
        final List<Object> argArray = args instanceof Object[] ? (args as Object[]) as List : [args]
        final Closure c = !argArray.isEmpty() && (argArray.first() instanceof Closure) ? argArray.first() as Closure : null
        if (c != null) {
            put(name, new VersionTransformer() {
                @Override def transform(final Object version) { c.call(version) }
            })
        }
    }


    def transform(@Nonnull final String name, def version) {
        def result = transformerMap[name]
        if (result == null) {
            throw new IllegalArgumentException("No transformation ${name} defined.")
        }
        result.transform(version)
    }

}
