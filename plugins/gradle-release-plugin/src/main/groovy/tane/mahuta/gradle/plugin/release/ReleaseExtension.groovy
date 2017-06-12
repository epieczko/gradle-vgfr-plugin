package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.MapEntryOrKeyValue
import org.gradle.api.Project
import tane.mahuta.gradle.plugin.vcs.VcsExtension
import tane.mahuta.gradle.plugin.version.VersionExtension

import javax.annotation.Nonnull
/**
 * Extension for the {@link tane.mahuta.gradle.plugin.ReleasePlugin}.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
class ReleaseExtension {

    final VcsExtension vcs

    final VersionExtension version

    private final Map<Project, Collection<String>> problems = [:]

    ReleaseExtension(@Nonnull final VcsExtension vcs, @Nonnull final VersionExtension version) {
        this.vcs = vcs
        this.version = version
    }

    ReleaseExtension addProblem(final Project project, final String problem) {
        problems[project] = problems[project] ?: [] as Collection<String>
        problems[project] << problem
        this
    }

    void eachProblem(@ClosureParams(MapEntryOrKeyValue.class) @Nonnull final Closure consumer) {
        problems.each(consumer)
    }

    boolean hasProblems() {
        !problems.isEmpty()
    }

}
