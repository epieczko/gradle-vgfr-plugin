package tane.mahuta.buildtools.version;

import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Semantic version representation.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.05.17.
 */
@EqualsAndHashCode(callSuper = true)
public class DefaultSemanticVersion extends AbstractSemanticVersion implements TransformationOperations<DefaultSemanticVersion> {

    /**
     * The semantic versioning pattern including the qualifier
     */
    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?(-.+?)?");

    private final String qualifier;
    private String stringRepresentation;

    /**
     * Create a new semantic version for the provided arguments.
     *
     * @param major     the major version
     * @param minor     the minor version
     * @param micro     the micro version (optional)
     * @param qualifier the qualifier (optional)
     */
    public DefaultSemanticVersion(@Nonnull final int major, @Nonnull final int minor,
                                  @Nullable final Integer micro, @Nullable final String qualifier) {
        super(major, minor, micro);
        this.qualifier = qualifier;
    }

    @Override
    public Serializable toStorable() {
        return toString();
    }

    /**
     * Parse the provided version string into a {@link DefaultSemanticVersion}.
     *
     * @param version the string representation
     * @return the parsed version
     */
    @Nullable
    public static DefaultSemanticVersion parse(final String version) {
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

        return new DefaultSemanticVersion(major, minor, micro, qualifier);
    }

    @Override
    public String getQualifier() {
        return qualifier;
    }

    @Override
    public DefaultSemanticVersion toRelease() {
        return new DefaultSemanticVersion(major, minor, micro, isSnapshot() ? null : getQualifier());
    }

    @Override
    public DefaultSemanticVersion toNextSnapshot(@Nullable final ChangeLevel l) {
        final ChangeLevel changeLevel = Optional.ofNullable(l).orElse(ChangeLevel.API_EXTENSION);
        int major = this.major, minor = this.minor;
        Integer micro = this.micro;
        switch (changeLevel) {
            case API_INCOMPATIBILITY:
                major++;
                break;
            case API_EXTENSION:
                minor++;
                break;
            case IMPLEMENTATION_CHANGED:
                if (micro != null) {
                    micro++;
                }
                break;
        }
        return new DefaultSemanticVersion(major, minor, micro, SNAPSHOT_QUALIFIER);
    }

    @Override
    protected String toStringRepresentation() {
        return this.stringRepresentation = Optional.ofNullable(this.stringRepresentation).orElseGet(() -> createStringRepresentation(major, minor, micro, qualifier));
    }
}
