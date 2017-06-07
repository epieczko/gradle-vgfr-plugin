package tane.mahuta.gradle.plugin.vcs

import tane.mahuta.buildtools.vcs.VcsAccessor

import javax.annotation.Nonnull

/**
 * The extension for the {@link tane.mahuta.gradle.plugin.VcsPlugin}.
 *
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class VcsExtension {

    @Delegate
    private final VcsAccessor accessor

    VcsExtension(@Nonnull final VcsAccessor accessor) {
        this.accessor = accessor
    }

}
