package tane.mahuta.gradle.plugin.version;

import groovy.lang.Closure;
import groovy.transform.CompileStatic;
import tane.mahuta.buildtools.version.VersionTransformer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Factory for {@link VersionTransformer}s.
 *
 * @author christian.heike@icloud.com
 *         Created on 08.06.17.
 */
@CompileStatic
public class VersionTransformerFactory {

    private VersionTransformerFactory() {
    }

    @Nullable
    public static VersionTransformer create(@Nonnull final Closure<?> transformer) {

        final Class[] paramTypes = transformer.getParameterTypes();
        if (paramTypes.length == 0) {
            return null;
        }

        return (version, args) -> {

            final List<Object> callArgs = new ArrayList<>(Arrays.asList(version));
            final int length = Math.min(args.length, paramTypes.length - 1);
            for (int i = 0; i < length; i++) {
                callArgs.add(args[i]);
            }
            final List<Class> argClasses = callArgs.stream().map(o -> Optional.ofNullable(o).map(Object::getClass).orElse(null)).collect(Collectors.toList());
            if (!argumentsMatchTypes(argClasses, paramTypes)) {
                throw new IllegalArgumentException("Expected argument classes " + Arrays.asList(paramTypes) + " but got: " + argClasses);
            }
            return transformer.call(callArgs.toArray());
        };
    }

    private static boolean argumentsMatchTypes(@Nonnull final List<Class> argClasses, @Nonnull final Class[] paramTypes) {
        for (int i = 0; i < argClasses.size(); i++) {
            if (!Optional.ofNullable(argClasses.get(i)).map(paramTypes[i]::isAssignableFrom).orElse(true)) {
                return false;
            }
        }
        return true;
    }

}
