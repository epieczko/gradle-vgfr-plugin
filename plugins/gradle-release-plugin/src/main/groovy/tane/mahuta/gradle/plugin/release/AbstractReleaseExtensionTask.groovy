package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.internal.AbstractTask

/**
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
abstract class AbstractReleaseExtensionTask extends AbstractTask {

    protected ReleaseExtension getReleaseExtension() {
        project.extensions.findByType(ReleaseExtension)
    }

}
