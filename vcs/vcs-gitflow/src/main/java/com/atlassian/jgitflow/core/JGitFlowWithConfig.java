package com.atlassian.jgitflow.core;

import lombok.Getter;

import javax.annotation.Nonnull;

/**
 * @author christian.heike@icloud.com
 *         Created on 07.06.17.
 */
public class JGitFlowWithConfig {

    @Getter(onMethod = @__(@Nonnull))
    private final JGitFlow jGitFlow;

    @Getter(onMethod = @__(@Nonnull))
    private final GitFlowConfiguration configuration;

    public JGitFlowWithConfig(@Nonnull final JGitFlow jGitFlow) {
        this.configuration = new GitFlowConfiguration(jGitFlow.git());
        this.jGitFlow = new JGitFlow(jGitFlow.git(), configuration);
    }
}
