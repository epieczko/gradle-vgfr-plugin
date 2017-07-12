package tane.mahuta.buildtools.semver;

import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Semantic version representation.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.05.17.
 */
@EqualsAndHashCode(callSuper = true)
public class DefaultSemanticVersion extends AbstractSemanticVersion {

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
    public String getQualifier() {
        return qualifier;
    }

    @Override
    protected String toStringRepresentation() {
        return this.stringRepresentation = Optional.ofNullable(this.stringRepresentation).orElseGet(() -> createStringRepresentation(major, minor, micro, qualifier));
    }

    @Override
    @Nonnull
    public DefaultSemanticVersion withMajor(final int major) {
        return new DefaultSemanticVersion(major, minor, micro, qualifier);
    }

    @Override
    @Nonnull
    public DefaultSemanticVersion withMinor(final int minor) {
        return new DefaultSemanticVersion(major, minor, micro, qualifier);
    }

    @Override
    @Nonnull
    public DefaultSemanticVersion withMicro(@Nullable final Integer micro) {
        return new DefaultSemanticVersion(major, minor, micro, qualifier);
    }

    @Nonnull
    public DefaultSemanticVersion withQualifier(@Nullable final  String qualifier) {
        return new DefaultSemanticVersion(major, minor, micro, qualifier);
    }
}
