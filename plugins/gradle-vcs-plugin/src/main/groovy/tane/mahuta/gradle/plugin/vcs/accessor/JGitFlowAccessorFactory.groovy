package tane.mahuta.gradle.plugin.vcs.accessor

import com.atlassian.jgitflow.core.InitContext
import com.atlassian.jgitflow.core.JGitFlow
import com.atlassian.jgitflow.core.JGitFlowWithConfig
import groovy.transform.CompileStatic
import org.eclipse.jgit.lib.RepositoryBuilder
import org.gradle.api.Project
import tane.mahuta.buildtools.vcs.VcsAccessor
import tane.mahuta.buildtools.vcs.accessor.JGitFlowAccessor
import tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory

import javax.annotation.Nonnull

/**
 * {@link VcsAccessorFactory} which factors {@link JGitFlowAccessor}s.
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
@CompileStatic
class JGitFlowAccessorFactory implements VcsAccessorFactory {

    static final InitContext DEFAULT_INITIAL_CTX = new InitContext().setVersiontag("version/")

    @Override
    VcsAccessor create(@Nonnull final Project project) {
        final gitDir = new RepositoryBuilder().readEnvironment().findGitDir(project.projectDir).gitDir
        gitDir?.exists() ? new JGitFlowAccessor(new JGitFlowWithConfig(JGitFlow.getOrInit(project.projectDir, DEFAULT_INITIAL_CTX))) : null
    }

}
