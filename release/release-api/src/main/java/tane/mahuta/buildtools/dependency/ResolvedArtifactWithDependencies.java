package tane.mahuta.buildtools.dependency;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A {@link ResolvedArtifact} with its dependencies.
 *
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
public interface ResolvedArtifactWithDependencies extends ResolvedArtifact {

    /**
     * @return the class path dependencies of the resolved artifact
     */
    @Nonnull
    Set<ResolvedArtifact> getClasspathDependencies();

}
