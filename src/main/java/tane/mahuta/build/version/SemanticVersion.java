package tane.mahuta.build.version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Semantic version object.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.05.17.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SemanticVersion implements Comparable<SemanticVersion> {

    private static final Function<SemanticVersion, Integer> MAJOR_SUPPLIER = SemanticVersion::getMajor,
            MINOR_SUPPLIER = SemanticVersion::getMinor,
            MICRO_SUPPLIER = SemanticVersion::getMicro,
            SNAPSHOT_SUPPLIER = v -> v.isSnapshot() ? 0 : 1;

    private static final Function<SemanticVersion, Integer>[] SUPPLIERS = new Function[]{
            MAJOR_SUPPLIER, MINOR_SUPPLIER, MICRO_SUPPLIER, SNAPSHOT_SUPPLIER
    };

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
    private final String qualifier;
    private String stringRepresentation;

    @Override
    public String toString() {
        return stringRepresentation = Optional.ofNullable(stringRepresentation).orElseGet(this::buildStringRepresentation);
    }

    @Nonnull
    private String buildStringRepresentation() {
        final StringBuilder sb = new StringBuilder();
        sb.append(major);
        sb.append(".");
        sb.append(minor);
        Optional.ofNullable(micro).ifPresent(m -> {
            sb.append(".");
            sb.append(m);
        });
        Optional.ofNullable(qualifier).ifPresent(q -> {
                    sb.append("-");
                    sb.append(q);
                }
        );
        return sb.toString();
    }

    /**
     * @return {@code true} if this denotes a snapshot version
     */
    public boolean isSnapshot() {
        return Optional.ofNullable(qualifier).map(q -> q.equals("SNAPSHOT")).orElse(false);
    }

    @Override
    public int compareTo(final SemanticVersion o) {
        if (o == null) {
            return 1;
        }
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
        final String snapshot = Optional.ofNullable(m.group(4)).map(s -> s.substring(1)).orElse(null);

        return new SemanticVersion(major, minor, micro, snapshot, null);
    }


}
