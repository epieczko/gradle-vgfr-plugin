package tane.mahuta.gradle.plugins.vcs.accessor

import com.atlassian.jgitflow.core.PatchedJGitFlow
import groovy.transform.CompileStatic
import org.eclipse.jgit.lib.Constants
import tane.mahuta.build.vcs.VcsAccessor
import tane.mahuta.build.vcs.VcsFlowConfig

import javax.annotation.Nonnull

/**
 * {@link VcsAccessor} using {@link com.atlassian.jgitflow.core.JGitFlow}.
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@CompileStatic
class JGitFlowAccessor implements VcsAccessor {

    private final PatchedJGitFlow jGitFlow

    final VcsFlowConfig branchConfig

    protected JGitFlowAccessor(@Nonnull final PatchedJGitFlow jGitFlow) {
        this.jGitFlow = jGitFlow
        this.branchConfig = new JGitFlowConfig(jGitFlow)
    }

    @Override
    String getBranch() {
        jGitFlow.git().repository.branch
    }

    @Override
    String getRevisionId() {
        jGitFlow.git().repository.resolve(Constants.HEAD) as String
    }

}
