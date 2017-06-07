package tane.mahuta.buildtools.vcs.accessor;

import com.atlassian.jgitflow.core.JGitFlowWithConfig;
import lombok.Getter;
import lombok.SneakyThrows;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * {@link VcsAccessor} using {@link com.atlassian.jgitflow.core.JGitFlow}.
 *
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
public class JGitFlowAccessor implements VcsAccessor {

    private final JGitFlowWithConfig jGitFlowWithConfig;

    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final VcsFlowConfig flowConfig;

    public JGitFlowAccessor(@Nonnull final JGitFlowWithConfig jGitFlowWithConfig) {
        this.jGitFlowWithConfig = jGitFlowWithConfig;
        this.flowConfig = new JGitFlowConfig(jGitFlowWithConfig.getConfiguration());
    }

    @Override
    @Nullable
    @SneakyThrows
    public String getBranch() {
        return jGitFlowWithConfig.getJGitFlow().git().getRepository().getBranch();
    }

    @Override
    @Nullable
    @SneakyThrows
    public String getRevisionId() {
        return Optional.ofNullable(jGitFlowWithConfig.getJGitFlow().git().getRepository().resolve(Constants.HEAD)).map(AnyObjectId::name).orElse(null);
    }

}
