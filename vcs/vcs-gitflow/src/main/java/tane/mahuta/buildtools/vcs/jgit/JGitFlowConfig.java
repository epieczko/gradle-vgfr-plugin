package tane.mahuta.buildtools.vcs.jgit;

import com.atlassian.jgitflow.core.GitFlowConfiguration;
import com.atlassian.jgitflow.core.JGitFlowConstants;
import lombok.SneakyThrows;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;

import javax.annotation.Nonnull;

/**
 * {@link VcsFlowConfig} for {@link com.atlassian.jgitflow.core.JGitFlow}.
 *
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
public class JGitFlowConfig implements VcsFlowConfig {

    @Nonnull
    private final GitFlowConfiguration flowConfiguration;

    JGitFlowConfig(@Nonnull final GitFlowConfiguration flowConfiguration) {
        this.flowConfiguration = flowConfiguration;
    }

    @Override
    public String getProductionBranch() {
        return flowConfiguration.getMaster();
    }

    @Override
    @SneakyThrows
    public VcsFlowConfig setProductionBranch(final String production) {
        flowConfiguration.setMaster(production);
        return this;
    }

    @Override
    public String getDevelopmentBranch() {
        return flowConfiguration.getDevelop();
    }

    @Override
    @SneakyThrows
    public VcsFlowConfig setDevelopmentBranch(final String development) {
        flowConfiguration.setDevelop(development);
        return this;
    }

    @Override
    public String getFeatureBranchPrefix() {
        return flowConfiguration.getPrefixValue(JGitFlowConstants.PREFIXES.FEATURE.configKey());
    }

    @Override
    @SneakyThrows
    public VcsFlowConfig setFeatureBranchPrefix(final String featurePrefix) {
        flowConfiguration.setPrefix(JGitFlowConstants.PREFIXES.FEATURE.configKey(), featurePrefix);
        return this;
    }

    @Override
    public String getReleaseBranchPrefix() {
        return flowConfiguration.getPrefixValue(JGitFlowConstants.PREFIXES.RELEASE.configKey());
    }

    @Override
    @SneakyThrows
    public VcsFlowConfig setReleaseBranchPrefix(final String releasePrefix) {
        flowConfiguration.setPrefix(JGitFlowConstants.PREFIXES.RELEASE.configKey(), releasePrefix);
        return this;
    }

    @Override
    public String getSupportBranchPrefix() {
        return flowConfiguration.getPrefixValue(JGitFlowConstants.PREFIXES.SUPPORT.configKey());
    }

    @Override
    @SneakyThrows
    public VcsFlowConfig setSupportBranchPrefix(final String supportPrefix) {
        flowConfiguration.setPrefix(JGitFlowConstants.PREFIXES.SUPPORT.configKey(), supportPrefix);
        return this;
    }

    @Override
    public String getHotfixBranchPrefix() {
        return flowConfiguration.getPrefixValue(JGitFlowConstants.PREFIXES.HOTFIX.configKey());
    }

    @Override
    @SneakyThrows
    public VcsFlowConfig setHotfixBranchPrefix(final String hotfixPrefix) {
        flowConfiguration.setPrefix(JGitFlowConstants.PREFIXES.HOTFIX.configKey(), hotfixPrefix);
        return this;
    }

    @Override
    public String getVersionTagPrefix() {
        return flowConfiguration.getPrefixValue(JGitFlowConstants.PREFIXES.VERSIONTAG.configKey());
    }

    @Override
    @SneakyThrows
    public VcsFlowConfig setVersionTagPrefix(final String versionTagPrefix) {
        flowConfiguration.setPrefix(JGitFlowConstants.PREFIXES.VERSIONTAG.configKey(), versionTagPrefix);
        return this;
    }

}
