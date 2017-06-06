package com.atlassian.jgitflow.core;

import com.atlassian.jgitflow.core.exception.AlreadyInitializedException;
import com.atlassian.jgitflow.core.exception.JGitFlowGitAPIException;
import com.atlassian.jgitflow.core.exception.JGitFlowIOException;
import com.atlassian.jgitflow.core.exception.SameBranchException;
import lombok.experimental.Delegate;
import org.eclipse.jgit.api.Git;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
public class PatchedJGitFlow extends JGitFlow {

    @Delegate
    private final GitFlowConfiguration configuration;

    PatchedJGitFlow(@Nonnull final Git git, @Nonnull final GitFlowConfiguration gfConfig) {
        super(git, gfConfig);
        this.configuration = gfConfig;
    }

    public static PatchedJGitFlow getOrInitPatched(final File projectDir, final InitContext initContext) throws JGitFlowIOException, AlreadyInitializedException, SameBranchException, JGitFlowGitAPIException {
        final JGitFlow jGitFlow = JGitFlow.getOrInit(projectDir, initContext);
        final GitFlowConfiguration configuration = new GitFlowConfiguration(jGitFlow.git());
        return new PatchedJGitFlow(jGitFlow.git(), configuration);
    }
}
