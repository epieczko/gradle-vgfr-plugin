package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
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

    ReleaseExtension(@Nonnull final VcsExtension vcs, @Nonnull final VersionExtension version) {
        this.vcs = vcs
        this.version = version
    }

}
