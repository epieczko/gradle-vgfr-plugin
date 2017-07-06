package tane.mahuta.buildtools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author christian.heike@icloud.com
 *         Created on 07.07.17.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogHelper {

    public static Object wrap(final Supplier<Object> b) {
        return new Object() {
            @Override
            public String toString() {
                return Optional.ofNullable(b).map(Supplier::get).map(Object::toString).orElse(null);
            }
        };
    }

}
