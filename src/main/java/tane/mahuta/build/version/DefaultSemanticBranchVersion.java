package tane.mahuta.build.version;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static lombok.AccessLevel.PRIVATE;

/**
 * {@link DefaultSemanticVersion} using a branch qualifier.
 *
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(exclude = "stringRepresentation")
public class DefaultSemanticBranchVersion implements SemanticBranchVersion {

    @NonNull
    @Delegate
    private final SemanticVersion decorated;

    @Getter(onMethod = @__({@Nullable, @Override}))
    private final String branchQualifier;

    /**
     * String representation
     */
    @NonNull
    private final String stringRepresentation;

    @Override
    public String toString() {
        return stringRepresentation;
    }

    /**
     * Create a new semantic version for the provided values.
     *
     * @param decorated       the {@link SemanticVersion} to be decorated
     * @param branchQualifier the branch qualifier
     * @return the object representing the values
     */
    public static SemanticBranchVersion of(@Nonnull final SemanticVersion decorated,
                                           @Nullable final String branchQualifier) {
        final String stringRepresentation = DefaultSemanticVersion.createStringRepresentation(decorated.getMajor(), decorated.getMinor(), decorated.getMicro(), branchQualifier, decorated.getQualifier());
        return SemanticVersionCache.getInstance().fromCacheOrCreate(stringRepresentation, SemanticBranchVersion.class, () -> new DefaultSemanticBranchVersion(decorated, branchQualifier, stringRepresentation));
    }

}
