package tane.mahuta.buildtools.version;


import lombok.EqualsAndHashCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * {@link DefaultSemanticVersion} using a branch qualifier.
 *
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
@EqualsAndHashCode(callSuper = true)
public class DefaultSemanticBranchVersion extends AbstractSemanticVersion
        implements SemanticBranchVersion, TransformationOperations<DefaultSemanticBranchVersion> {

    protected final String qualifier;
    protected final Supplier<String> branchQualifierSupplier;

    /**
     * Create a new semantic branch version of the provided {@link SemanticVersion} and a branch qualifier {@link Supplier}.
     *
     * @param decorated       the decorated semantic version
     * @param branchQualifier the branch qualifier supplier
     */
    public DefaultSemanticBranchVersion(@Nonnull final SemanticVersion decorated,
                                        @Nonnull final Supplier<String> branchQualifier) {
        this(decorated.getMajor(), decorated.getMinor(), decorated.getMicro(), branchQualifier, decorated.getQualifier());
    }

    /**
     * Create a new semantic branch version of the provided parameters.
     *
     * @param major                   the major version part
     * @param minor                   the minor version part
     * @param micro                   the micro version part
     * @param branchQualifierSupplier the supplier for the branch qualifier
     * @param qualifier               the qualifier for the version
     */
    public DefaultSemanticBranchVersion(int major, int minor, @Nullable Integer micro, @Nonnull final Supplier<String> branchQualifierSupplier,
                                        @Nullable final String qualifier) {
        super(major, minor, micro);
        this.branchQualifierSupplier = branchQualifierSupplier;
        this.qualifier = qualifier;
    }


    @Nullable
    @Override
    public String getBranchQualifier() {
        return branchQualifierSupplier.get();
    }

    @Override
    protected String toStringRepresentation() {
        return createStringRepresentation(major, minor, micro, branchQualifierSupplier.get(), qualifier);
    }

    @Override
    public int compareTo(final SemanticVersion other) {
        final int result = super.compareTo(other);
        if (result != 0) {
            return result; // If the version already differs, return it
        }
        final String thatBranchQualifier = other instanceof SemanticBranchVersion ? ((SemanticBranchVersion) other).getBranchQualifier() : null;
        if (getBranchQualifier() == null) {
            return thatBranchQualifier == null ? 0 : 1;
        } else {
            return thatBranchQualifier != null ? 0 : -1;
        }
    }

    /**
     * Parse the provided {@code version} to a {@link SemanticVersion} and add the provided {@code branch} qualifier.
     *
     * @param version the version string to be parsed
     * @param branch  the branch qualifier to be added
     * @return the {@link SemanticBranchVersion} for both parameters
     */
    @Nonnull
    public static DefaultSemanticBranchVersion parseWithBranchName(final String version, final String branch) {
        return new DefaultSemanticBranchVersion(DefaultSemanticVersion.parse(version), () -> branch);
    }

    @Override
    public String getQualifier() {
        return qualifier;
    }

    @Override
    public Object toStorable() {
        return createStringRepresentation(major, minor, micro, qualifier);
    }

    @Override
    public DefaultSemanticBranchVersion toRelease() {
        return new DefaultSemanticBranchVersion(major, minor, micro, branchQualifierSupplier, null);
    }

    @Override
    public DefaultSemanticBranchVersion toNextSnapshot(@Nullable final ChangeLevel l) {
        return new DefaultSemanticBranchVersion(new DefaultSemanticVersion(major, minor, micro, qualifier).toNextSnapshot(l), branchQualifierSupplier);
    }

}
