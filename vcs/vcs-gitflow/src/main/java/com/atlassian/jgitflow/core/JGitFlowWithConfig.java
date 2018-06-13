package com.atlassian.jgitflow.core;

import lombok.Getter;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;

import javax.annotation.Nonnull;

/**
 * Splits {@link GitFlowConfiguration} and {@link JGitFlow}.
 *
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
public class JGitFlowWithConfig extends JGitFlow {

    @Getter(onMethod = @__(@Nonnull))
    private final GitFlowConfiguration configuration;

    public JGitFlowWithConfig(@Nonnull final Git git, @Nonnull final InitContext initContext) {
        super(git, createAndApplyConfig(git, initContext));
        this.configuration = new GitFlowConfiguration(git);
    }

    @SneakyThrows
    private static GitFlowConfiguration createAndApplyConfig(@Nonnull final Git git, @Nonnull final InitContext initContext) {
        final GitFlowConfiguration result = new GitFlowConfiguration(git);
        if (!result.gitFlowIsInitialized()) {
            result.setDevelop(initContext.getDevelop());
            result.setMaster(initContext.getMaster());
            for (final JGitFlowConstants.PREFIXES prefix : JGitFlowConstants.PREFIXES.values()) {
                result.setPrefix(prefix.configKey(), initContext.getPrefix(prefix.name()));
            }
        }
        return result;
    }
}
