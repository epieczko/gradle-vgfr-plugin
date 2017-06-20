package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import tane.mahuta.gradle.plugin.vcs.VcsExtension
import tane.mahuta.gradle.plugin.version.VersioningExtension

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

    final VersioningExtension version

    final ProblemReport problems = new ProblemReport()

    ReleaseExtension(@Nonnull final Project project) {
        this.vcs = project.extensions.findByType(VcsExtension)
        this.version = project.extensions.findByType(VersioningExtension)
    }

}
