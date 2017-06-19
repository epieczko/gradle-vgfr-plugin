package tane.mahuta.buildtools.vcs.jgit;

import com.atlassian.jgitflow.core.InitContext;
import com.atlassian.jgitflow.core.JGitFlow;
import com.atlassian.jgitflow.core.JGitFlowWithConfig;
import lombok.SneakyThrows;
import org.eclipse.jgit.lib.RepositoryBuilder;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.vcs.VcsAccessorFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link VcsAccessorFactory} for {@link JGitFlowAccessor}.
 *
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
public class JGitFlowAccessorFactory implements VcsAccessorFactory {

    private static final InitContext DEFAULT_INITIAL_CTX = new InitContext().setVersiontag("version/");

    private Map<String, JGitFlowAccessor> cached = new HashMap<>();

    @Nullable
    @Override
    public VcsAccessor create(@Nonnull final File directory) {
        final File gitDir = new RepositoryBuilder().readEnvironment().findGitDir(directory).getGitDir();
        return Optional.ofNullable(gitDir)
                .map(JGitFlowAccessorFactory::nullIfNotExists)
                .map(this::getFormCacheOrCreate)
                .orElse(null);
    }

    @SneakyThrows
    private JGitFlowAccessor getFormCacheOrCreate(@Nonnull final File file) {
        final String key = file.getAbsoluteFile().getCanonicalPath();
        return Optional.ofNullable(cached.get(key)).orElseGet(() -> {
            final JGitFlowAccessor result = doCreate(file);
            cached.put(key, result);
            return result;
        });
    }

    @SneakyThrows
    protected JGitFlowAccessor doCreate(final File d) {
        return new JGitFlowAccessor(new JGitFlowWithConfig(JGitFlow.getOrInit(d, DEFAULT_INITIAL_CTX)));
    }

    @Nullable
    private static final File nullIfNotExists(@Nonnull final File f) {
        return f.exists() ? f : null;
    }

}
