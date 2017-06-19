package tane.mahuta.buildtools.version;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import tane.mahuta.buildtools.vcs.ServiceLoaderVcsAccessorFactory;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;

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
        return Optional.ofNullable(accessor.getBranch())
                .map(b -> mapBranchName(b, accessor.getFlowConfig()))
                .map(b -> b.replaceAll("[^a-zA-Z0-9]+", "_"))
                .map(b -> StringUtils.isBlank(b) ? null : b)
                .orElse(null);
    }

    /**
     * Map a branch name from the {@link VcsFlowConfig}.
     *
     * @param flowConfig the flow configuration
     * @param name       the branch name
     * @return the branch qualifier or {@code null} if not to use any
     */
    protected String mapBranchName(@Nonnull final String name, @Nonnull final VcsFlowConfig flowConfig) {
        if (name.startsWith(flowConfig.getFeatureBranchPrefix())) {
            return name.substring(flowConfig.getFeatureBranchPrefix().length());
        } else if (name.startsWith(flowConfig.getSupportBranchPrefix())) {
            return name.substring(flowConfig.getSupportBranchPrefix().length());
        } else if (name.equals(flowConfig.getProductionBranch()) || name.equals(flowConfig.getDevelopmentBranch()) ||
                name.startsWith(flowConfig.getReleaseBranchPrefix()) || name.startsWith(flowConfig.getHotfixBranchPrefix())) {
            return null;
        }
        return name;
    }

    private static class Holder {
        private static final DefaultSemanticBranchVersionParser INSTANCE = new DefaultSemanticBranchVersionParser();
    }

    public static DefaultSemanticBranchVersionParser getInstance() {
        return Holder.INSTANCE;
    }

}
