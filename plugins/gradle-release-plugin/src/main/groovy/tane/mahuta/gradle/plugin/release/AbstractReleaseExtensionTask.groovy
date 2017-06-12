package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.Input

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
abstract class AbstractReleaseExtensionTask extends AbstractTask {

    AbstractReleaseExtensionTask() {
        group = 'release'
    }

    @Input
    protected ReleaseExtension getReleaseExtension() {
        project.extensions.findByType(ReleaseExtension)
    }

}
