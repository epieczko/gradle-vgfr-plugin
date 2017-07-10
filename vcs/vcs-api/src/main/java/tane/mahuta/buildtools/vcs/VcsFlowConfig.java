package tane.mahuta.buildtools.vcs;

/**
 * Configuration for branches in the VCS.
 *
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
public interface VcsFlowConfig {

    /**
     * @return the production branch name
     */
    String getProductionBranch();

    /**
     * Set the production branch name.
     *
     * @param production the name of the branch
     * @return (this)
     */
    VcsFlowConfig setProductionBranch(String production);

    /**
     * @return the production branch name
     */
    String getDevelopmentBranch();

    /**
     * Set the development branch name.
     *
     * @param development the name of the branch
     * @return (this)
     */
    VcsFlowConfig setDevelopmentBranch(String development);

    /**
     * @return the prefix used for feature branches
     */
    String getFeatureBranchPrefix();

    /**
     * Set the prefix used for feature branches.
     *
     * @param featurePrefix the prefix to be set
     * @return (this)
     */
    VcsFlowConfig setFeatureBranchPrefix(String featurePrefix);

    /**
     * @return the prefix used for release branches
     */
    String getReleaseBranchPrefix();

    /**
     * Set the prefix for release branches.
     *
     * @param releasePrefix the prefix to be set
     * @return (this)
     */
    VcsFlowConfig setReleaseBranchPrefix(String releasePrefix);

    /**
     * @return the prefix used for support branches
     */
    String getSupportBranchPrefix();

    /**
     * Set the prefix for support branches.
     *
     * @param supportPrefix the prefix to be set
     * @return (this)
     */
    VcsFlowConfig setSupportBranchPrefix(String supportPrefix);

    /**
     * @return the prefix used for hotfix branches
     */
    String getHotfixBranchPrefix();

    /**
     * Set the prefix for hotfix branches.
     *
     * @param hotfixPrefix the prefix to be set
     * @return (this)
     */
    VcsFlowConfig setHotfixBranchPrefix(String hotfixPrefix);

    /**
     * @return the prefix for version tags
     */
    String getVersionTagPrefix();

    /**
     * Set the prefix for version tags.
     *
     * @param versionTagPrefix the prefix to be set
     * @return (this)
     */
    VcsFlowConfig setVersionTagPrefix(String versionTagPrefix);

}
