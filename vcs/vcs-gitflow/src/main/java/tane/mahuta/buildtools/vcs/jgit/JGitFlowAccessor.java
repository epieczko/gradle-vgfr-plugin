package tane.mahuta.buildtools.vcs.jgit;

import com.atlassian.jgitflow.core.JGitFlowWithConfig;
import lombok.Getter;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.vcs.VcsFlowConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @Nonnull
    @Override
    @SneakyThrows
    public Collection<String> getUncommittedFilePaths() {
        final Set<String> result = new HashSet<>();
        final Status status = jGitFlowWithConfig.getJGitFlow().git().status().call();
        result.addAll(status.getUncommittedChanges());
        result.addAll(status.getUntracked());
        return result;
    }

}
