package tane.mahuta.gradle.plugins.vcs.accessor

import com.atlassian.jgitflow.core.JGitFlowConstants
import com.atlassian.jgitflow.core.PatchedJGitFlow
import groovy.transform.CompileStatic
import tane.mahuta.build.vcs.VcsFlowConfig

import javax.annotation.Nonnull
/**
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@CompileStatic
class JGitFlowConfig implements VcsFlowConfig {

    private final PatchedJGitFlow jGitFlow

    JGitFlowConfig(final PatchedJGitFlow jGitFlow) {
        this.jGitFlow = jGitFlow
    }

    @Override
    String getProductionBranch() {
        jGitFlow.master
    }

    @Override
    VcsFlowConfig setProductionBranch(@Nonnull final String production) {
        jGitFlow.master = production
        this
    }

    @Override
    String getDevelopmentBranch() {
        jGitFlow.develop
    }

    @Override
    VcsFlowConfig setDevelopmentBranch(@Nonnull final String development) {
        jGitFlow.develop = development
        this
    }

    @Override
    String getFeatureBranchPrefix() {
        jGitFlow.getFeatureBranchPrefix()
    }

    @Override
    VcsFlowConfig setFeatureBranchPrefix(final String featurePrefix) {
        jGitFlow.setPrefix(JGitFlowConstants.PREFIXES.FEATURE.configKey(), featurePrefix)
        this
    }

    @Override
    String getReleaseBranchPrefix() {
        jGitFlow.releaseBranchPrefix
    }

    @Override
    VcsFlowConfig setReleaseBranchPrefix(final String releasePrefix) {
        jGitFlow.setPrefix(JGitFlowConstants.PREFIXES.RELEASE.configKey(), releasePrefix)
        this
    }

    @Override
    String getSupportBranchPrefix() {
        jGitFlow.supportBranchPrefix
    }

    @Override
    VcsFlowConfig setSupportBranchPrefix(final String supportPrefix) {
        jGitFlow.setPrefix(JGitFlowConstants.PREFIXES.SUPPORT.configKey(), supportPrefix)
        this
    }

    @Override
    String getHotfixBranchPrefix() {
        jGitFlow.hotfixBranchPrefix
    }

    @Override
    VcsFlowConfig setHotfixBranchPrefix(final String hotfixPrefix) {
        jGitFlow.setPrefix(JGitFlowConstants.PREFIXES.HOTFIX.configKey(), hotfixPrefix)
        this
    }

    @Override
    String getVersionTagPrefix() {
        jGitFlow.versionTagPrefix
    }

    @Override
    VcsFlowConfig setVersionTagPrefix(final String versionTagPrefix) {
        jGitFlow.setPrefix(JGitFlowConstants.PREFIXES.VERSIONTAG.configKey(), versionTagPrefix)
        this
    }
    
}
