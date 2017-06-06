package tane.mahuta.gradle.plugins.vcs.accessor

import com.atlassian.jgitflow.core.InitContext
import com.atlassian.jgitflow.core.PatchedJGitFlow
import groovy.transform.CompileStatic
import org.eclipse.jgit.lib.RepositoryBuilder
import org.gradle.api.Project
import tane.mahuta.build.vcs.VcsAccessor
import tane.mahuta.gradle.plugins.vcs.VcsAccessorFactory

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
        gitDir.exists() ? new JGitFlowAccessor(PatchedJGitFlow.getOrInitPatched(project.projectDir, DEFAULT_INITIAL_CTX)) : null
    }

}
