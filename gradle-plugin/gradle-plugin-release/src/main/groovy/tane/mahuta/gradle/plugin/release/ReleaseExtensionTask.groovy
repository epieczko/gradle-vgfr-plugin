package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserCodeException
import org.gradle.api.Project
import org.gradle.api.internal.AbstractTask
import tane.mahuta.buildtools.release.ReleaseStep

import javax.annotation.Nonnull

/**
 * {@link AbstractTask} providing the {@link ReleaseExtension}.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
class ReleaseExtensionTask extends AbstractTask {

    ReleaseExtensionTask() {
        group = 'release'
    }

    /**
     * Retrieve the release extension for the provided project.
     * @param project the project to get the extension for
     * @return the {@link ReleaseExtension} being used for releasing
     */
    protected ReleaseExtension releaseExtensionOf(@Nonnull final Project project) {
        project.extensions.findByType(ReleaseExtension)
    }

    /**
     * Invokes the provided step for each {@link ReleaseExtension#getArtifactReleases()}.
     * @param step the step to invoke
     */
    void invokeStep(@Nonnull final ReleaseStep step) {
        project.allprojects { p ->
            final extension = releaseExtensionOf(p)
            if (extension != null) {
                throw new InvalidUserCodeException("The ${p} has no release extension applied, this is probably due to a wrong configuration.")
            }
            extension.artifactReleases.each(step.&apply.ncurry(1, extension.infrastructure))
        }
    }

}
