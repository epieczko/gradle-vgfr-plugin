package tane.mahuta.build.version;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
@EqualsAndHashCode(exclude = "stringRepresentation")
public class DefaultSemanticVersion implements SemanticVersion {

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
     * The semantic versioning pattern including the qualifier
     */
    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?(-.+?)?");

    @Getter(onMethod = @__(@Override))
    private final int major;

    @Getter(onMethod = @__(@Override))
    private final int minor;

    @Getter(onMethod = @__(@Override))
    private final Integer micro;

    @Getter(onMethod = @__(@Override))
    private final String qualifier;

    /**
     * String representation
     */
    private final String stringRepresentation;

    @Override
    public String toString() {
        return stringRepresentation;
    }

    @Override
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
     * Parse the provided version string into a {@link DefaultSemanticVersion}.
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
     *
     * @param major     the major version
     * @param minor     the minor version
     * @param micro     the micro version (optional)
     * @param qualifier the qualifier
     * @return the object representing the values
     */
    public static SemanticVersion of(final int major, final int minor, final Integer micro, final String qualifier) {
        final String stringRepresentation = createStringRepresentation(major, minor, micro, qualifier);
        return SemanticVersionCache.getInstance().fromCacheOrCreate(stringRepresentation, SemanticVersion.class, () -> new DefaultSemanticVersion(major, minor, micro, qualifier, stringRepresentation));
    }

    /**
     * Create a string representation from the provided values.
     *
     * @param major      the major version
     * @param minor      the minor version
     * @param micro      the micro version (optional)
     * @param qualifiers the qualifier (optional)
     * @return the string builder provided
     */
    @Nonnull
    static String createStringRepresentation(final int major, final int minor, final Integer micro, final String... qualifiers) {
        final StringBuilder sb = new StringBuilder();
        sb.append(major).append(".").append(minor);
        Optional.ofNullable(micro).ifPresent(m -> {
            sb.append(".");
            sb.append(m);
        });
        Arrays.stream(qualifiers).filter(StringUtils::isNotBlank).forEach(q -> {
            sb.append("-");
            sb.append(q);
        });
        return sb.toString();
    }

}
