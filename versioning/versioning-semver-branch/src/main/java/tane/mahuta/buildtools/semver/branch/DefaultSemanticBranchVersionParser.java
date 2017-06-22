package tane.mahuta.buildtools.semver.branch;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import tane.mahuta.buildtools.semver.AbstractSemanticVersionParser;
import tane.mahuta.buildtools.vcs.ServiceLoaderVcsAccessorFactory;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;
import tane.mahuta.buildtools.version.SemanticBranchVersion;
import tane.mahuta.buildtools.version.VersionParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Optional;

import static lombok.AccessLevel.PROTECTED;

/**
 * Implementation of the {@link VersionParser} for {@link DefaultSemanticBranchVersion}s.
 *
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
@AllArgsConstructor(access = PROTECTED)
public class DefaultSemanticBranchVersionParser extends AbstractSemanticVersionParser<SemanticBranchVersion> {

    @Nonnull
    @Override
    protected SemanticBranchVersion doCreate(final int major, final int minor, @Nullable final Integer micro,
                                             @Nullable final String qualifier, @NonNull final File sourceDirectory) {
        final VcsAccessor accessor = Optional.ofNullable(ServiceLoaderVcsAccessorFactory.getInstance().create(sourceDirectory))
                .orElseThrow(() -> new IllegalArgumentException("Could not create a VCS accessor for source directory: " + sourceDirectory));

        return new DefaultSemanticBranchVersion(major, minor, micro, () -> this.createBranchQualifier(accessor), qualifier);
    }

    /**
     * Create a branch qualifier from the {@link VcsAccessor}.
     *
     * @param accessor the vcs accessor
     * @return the branch qualifier or {@code null} if not to use any
     */
    protected String createBranchQualifier(@Nonnull final VcsAccessor accessor) {
        return Optional.ofNullable(mapBranchName(accessor))
                .map(b -> b.replaceAll("[^a-zA-Z0-9]+", "_"))
                .map(b -> StringUtils.isBlank(b) ? null : b)
                .orElse(null);
    }

    /**
     * Map a branch name from the {@link VcsFlowConfig}.
     *
     * @param accessor the vcs accessor
     * @return the branch qualifier or {@code null} if not to use any
     */
    protected String mapBranchName(@Nonnull final VcsAccessor accessor) {

        final VcsFlowConfig flowConfig = accessor.getFlowConfig();
        final String featureBranchPrefix = flowConfig.getFeatureBranchPrefix();
        final String supportBranchPrefix = flowConfig.getSupportBranchPrefix();

        if (accessor.isOnBranch(featureBranchPrefix)) {

            return accessor.removeBranchPrefix(featureBranchPrefix);

        } else if (accessor.isOnBranch(supportBranchPrefix)) {

            return accessor.removeBranchPrefix(supportBranchPrefix);

        } else if (accessor.isOnBranch(flowConfig.getProductionBranch()) ||
                accessor.isOnBranch(flowConfig.getDevelopmentBranch()) ||
                accessor.isOnBranch(flowConfig.getReleaseBranchPrefix()) ||
                accessor.isOnBranch(flowConfig.getHotfixBranchPrefix())) {

            return null;
        }

        return accessor.getBranch();
    }

    private static class Holder {
        private static final DefaultSemanticBranchVersionParser INSTANCE = new DefaultSemanticBranchVersionParser();
    }

    public static DefaultSemanticBranchVersionParser getInstance() {
        return Holder.INSTANCE;
    }

}
