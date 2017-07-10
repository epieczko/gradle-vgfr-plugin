package tane.mahuta.buildtools.release;

import tane.mahuta.buildtools.dependency.ArtifactWithClasspath;
import tane.mahuta.buildtools.dependency.DependencyContainer;
import tane.mahuta.buildtools.release.reporting.ReleaseProblem;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Interface for a release of an artifact.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public interface ArtifactRelease extends ArtifactWithClasspath {

    /**
     * @return the project directory for the artifact
     */
    @Nonnull
    File getProjectDir();

    /**
     * @return all dependency containers being used by the project
     */
    @Nonnull
    Set<? extends DependencyContainer> getDependencyContainers();

    /**
     * Add a reporting by using the {@link ReleaseProblem.ReleaseProblemBuilder}.
     *
     * @param problemDescriptor the {@link Consumer} which configures the builder
     */
    void describeProblem(@Nonnull Consumer<ReleaseProblem.ReleaseProblemBuilder> problemDescriptor);

    /**
     * @return an unmodifiable {@link List} of {@link ReleaseProblem}s for the artifact release.
     */
    @Nonnull
    List<ReleaseProblem> getProblems();

}
