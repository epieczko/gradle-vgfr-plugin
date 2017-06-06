package tane.mahuta.build.version;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

/**
 * Semantic version representation.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.05.17.
 */
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(of = "stringRepresentation")
public class SemanticVersion implements Version, Comparable<SemanticVersion> {

    private static final Function<SemanticVersion, Integer>
            MAJOR_SUPPLIER = SemanticVersion::getMajor,
            MINOR_SUPPLIER = SemanticVersion::getMinor,
            MICRO_SUPPLIER = SemanticVersion::getMicro,
            SNAPSHOT_SUPPLIER = v -> v.isSnapshot() ? 0 : 1;

    /**
     * Ordered accessors for the comparison
     */
    private static final List<Function<SemanticVersion, Integer>> SUPPLIERS = Arrays.asList(
            MAJOR_SUPPLIER, MINOR_SUPPLIER, MICRO_SUPPLIER, SNAPSHOT_SUPPLIER
    );

    /**
     * A cache for the semantic versions
     */
    private static Map<String, SoftReference<SemanticVersion>> CACHE = new ConcurrentHashMap<>();

    /**
     * The semantic versioning pattern including the qualifier
     */
    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?(-.+?)?");

    /**
     * Major version part
     */
    @Getter
    private final int major;

    /**
     * Minor version part
     */
    @Getter
    private final int minor;

    /**
     * micro version part
     */
    @Getter
    private final Integer micro;

    /**
     * the qualifier from the version
     */
    @Getter
    private final String qualifier;

    /**
     * String representation cache
     */
    private final String stringRepresentation;

    @Override
    public String toString() {
        return stringRepresentation;
    }

    /**
     * @return {@code true} if this denotes a snapshot version
     */
    public boolean isSnapshot() {
        return Optional.ofNullable(qualifier).map(q -> q.equals("SNAPSHOT")).orElse(false);
    }

    @Override
    public int compareTo(@Nonnull final SemanticVersion o) {
        for (final Function<SemanticVersion, Integer> supplier : SUPPLIERS) {
            final Integer thisPart = Optional.ofNullable(supplier.apply(this)).orElse(0);
            final Integer otherPart = Optional.ofNullable(supplier.apply(o)).orElse(0);
            final int partRes = thisPart.compareTo(otherPart);
            if (partRes != 0) {
                return partRes;
            }
        }
        return 0;
    }

    @Override
    public Serializable toStorable() {
        return stringRepresentation;
    }

    /**
     * Parse the provided version string into a {@link SemanticVersion}.
     *
     * @param version the string representation
     * @return the parsed version
     */
    @Nullable
    public static SemanticVersion parse(final String version) {
        if (version == null) {
            return null;
        }

        final Matcher m = PATTERN.matcher(version);
        if (!m.matches()) {
            throw new IllegalArgumentException("Could not parse version string to semantic version: " + version);
        }

        final int major = Integer.parseInt(m.group(1));
        final int minor = Integer.parseInt(m.group(2));
        final Integer micro = Optional.ofNullable(m.group(3)).map(s -> s.substring(1)).map(Integer::parseInt).orElse(null);
        final String qualifier = Optional.ofNullable(m.group(4)).map(s -> s.substring(1)).orElse(null);
        return of(major, minor, micro, qualifier);
    }

    /**
     * Create a new semantic version for the provided values.
     * @param major the major version
     * @param minor the minor version
     * @param micro the micro version (optional)
     * @param qualifier the qualifier
     * @return the object representing the values
     */
    public static SemanticVersion of(final int major, final int minor, final Integer micro, final String qualifier) {
        final String stringRepresentation = buildStringRepresentation(major, minor, micro, qualifier);
        return Optional.ofNullable(CACHE.get(stringRepresentation)).map(SoftReference::get).orElseGet(() -> createAndCache(major, minor, micro, qualifier, stringRepresentation));
    }

    /**
     * Builds a string representation for the provided values.
     * @see SemanticVersion#of(int, int, Integer, String)
     * @return the string representation for the parameters
     */
    @Nonnull
    private static String buildStringRepresentation(final int major, final int minor, final Integer micro, final String qualifier) {
        final StringBuilder sb = new StringBuilder();
        sb.append(major).append(".").append(minor);
        Optional.ofNullable(micro).ifPresent(m -> {
            sb.append(".");
            sb.append(m);
        });
        Optional.ofNullable(qualifier).ifPresent(q -> {
            sb.append("-");
            sb.append(q);
        });
        return sb.toString();
    }

    /**
     * @see SemanticVersion#of(int, int, Integer, String)
     * @param stringRepresentation the string representation as cache key
     * @return the version representation
     */
    private static SemanticVersion createAndCache(final int major, final int minor, final Integer micro,
                                                  final String qualifier, @Nonnull final String stringRepresentation) {
        final SemanticVersion result = new SemanticVersion(major, minor, micro, qualifier, stringRepresentation);
        CACHE.put(stringRepresentation, new SoftReference<>(result));
        return result;
    }

}
