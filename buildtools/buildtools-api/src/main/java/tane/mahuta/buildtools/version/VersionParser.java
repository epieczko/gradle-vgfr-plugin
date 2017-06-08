package tane.mahuta.buildtools.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Interface for a parser for {@link Version}.
 *
 * @param <V> the concrete type of the parsed version
 * @author christian.heike@icloud.com
 *         Created on 04.06.17.
 */
public interface VersionParser<V extends Version> {

    /**
     * Parse the provided source to a version.
     *
     * @param source the source to be parsed
     * @return the parsed version
     */
    @Nullable
    V parse(@Nullable Object source);

    /**
     * Decorate the provided parser, wrapping the parse method into the parse method of the provided decorator.
     * @param decorator the decorator
     * @param <T> the type of the decorated version
     * @return the decorated parser
     */
    default <T extends Version> VersionParser<T> decorate(@Nonnull final VersionParser<T> decorator) {
        return source -> Optional.ofNullable(parse(source)).map(decorator::parse).orElse(null);
    }
}
