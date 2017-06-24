package tane.mahuta.buildtools.release.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tane.mahuta.buildtools.release.ArtifactRelease;
import tane.mahuta.buildtools.release.ReleaseInfrastructure;
import tane.mahuta.buildtools.release.ReleaseStep;
import tane.mahuta.buildtools.release.reporting.Severity;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A {@link ReleaseStep} which checks, if the branch we are currently on is releasable.
 *
 * @author christian.heike@icloud.com
 *         Created on 23.06.17.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReleasableBranchCheck implements ReleaseStep {

    private static final List<Function<VcsFlowConfig, String>> DEFAULT_RELEASABLE_BRANCHES = Arrays.asList(
            VcsFlowConfig::getDevelopmentBranch,
            VcsFlowConfig::getHotfixBranchPrefix,
            VcsFlowConfig::getSupportBranchPrefix,
            VcsFlowConfig::getReleaseBranchPrefix
    );

    private static class InstanceHolder {
        private static final ReleasableBranchCheck INSTANCE = new ReleasableBranchCheck();
    }

    public static ReleasableBranchCheck getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void apply(@Nonnull final ArtifactRelease release, @Nonnull final ReleaseInfrastructure releaseInfrastructure) {
        final VcsAccessor vcs = releaseInfrastructure.getVcs();
        final String branch = vcs.getBranch();
        if (branch != null) {
            if (!supportedReleaseBranchNames(vcs.getFlowConfig()).anyMatch(vcs::isOnBranch)) {
                release.describeProblem(b -> b.severity(Severity.PROBLEM)
                        .messageFormat("Not on a releasable branch: {}")
                        .formatArgs(branch));
            }
        }
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Checks if the release is happening on a releasable branch";
    }

    /**
     * Get the releasable branch names and prefixes from the {@link VcsFlowConfig}.
     *
     * @param flowConfig the flow configuration
     * @return a stream of supported branch names or prefixes
     */
    @Nonnull
    protected Stream<String> supportedReleaseBranchNames(@Nonnull final VcsFlowConfig flowConfig) {
        return DEFAULT_RELEASABLE_BRANCHES.stream().map(f -> f.apply(flowConfig));
    }

}
