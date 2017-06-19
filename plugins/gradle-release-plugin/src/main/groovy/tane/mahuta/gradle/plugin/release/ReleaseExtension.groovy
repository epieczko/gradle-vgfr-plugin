package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import tane.mahuta.buildtools.version.ChangeLevel
import tane.mahuta.gradle.plugin.vcs.VcsExtension
import tane.mahuta.gradle.plugin.version.VersioningExtension

import javax.annotation.Nonnull
import javax.annotation.Nullable
/**
 * Extension for the {@link tane.mahuta.gradle.plugin.ReleasePlugin}.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
class ReleaseExtension {

    final VcsExtension vcs

    final VersioningExtension version

    final ProblemReport problems = new ProblemReport()

    private ChangeLevelAnalyzer changeLevelAnalyzer

    ReleaseExtension(@Nonnull final Project project) {
        this.vcs = project.extensions.findByType(VcsExtension)
        this.version = project.extensions.findByType(VersioningExtension)
    }

    /**
     * Determines the {@link ChangeLevel} of the project.
     * @param project the project
     * @return the change level
     */
    @Nullable
    ChangeLevel determineChangeLevel(@Nonnull final Project project) {
        changeLevelAnalyzer?.determineChangeLevel(project)
    }

    /**
     * Set the {@link ChangeLevelAnalyzer}.
     * @param changeLevelAnalyzer the analyzer to be used
     */
    void setChangeLevelAnalyzer(@Nullable final ChangeLevelAnalyzer changeLevelAnalyzer) {
        this.changeLevelAnalyzer = changeLevelAnalyzer
    }

    def releaseVersionOf(final Project project) {

    }
}
