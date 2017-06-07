package tane.mahuta.buildtools.version;

import javax.annotation.Nullable;

/**
 * A {@link SemanticVersion} is enhanced by a qualifier for branches.
 *
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
public interface SemanticBranchVersion extends SemanticVersion {

    /**
     *
     * @return the qualifier for the branch
     */
    @Nullable
    String getBranchQualifier();
}
