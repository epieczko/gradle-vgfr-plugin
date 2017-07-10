package tane.mahuta.buildtools.release;


import tane.mahuta.buildtools.release.reporting.ReleaseProblem;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Abstract implementation of {@link ArtifactRelease} providing a handler for {@link #describeProblem(Consumer)}.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public abstract class AbstractArtifactRelease implements ArtifactRelease {

    private final List<ReleaseProblem> problems = new ArrayList<>();

    @Override
    public void describeProblem(@Nonnull final Consumer<ReleaseProblem.ReleaseProblemBuilder> problemDescriptor) {
        final ReleaseProblem.ReleaseProblemBuilder builder = ReleaseProblem.builder();
        problemDescriptor.accept(builder);
        problems.add(builder.build());
    }

    @Nonnull
    @Override
    public List<ReleaseProblem> getProblems() {
        return Collections.unmodifiableList(problems);
    }

}
