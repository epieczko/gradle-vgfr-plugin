package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.tasks.TaskAction
/**
 * @author christian.heike@icloud.com
 * Created on 12.06.17.
 */
@CompileStatic
class ReleaseCheckAlreadyReleasedTask extends AbstractReleaseExtensionTask {

    @TaskAction
    void checkProjectsNotAlreadyReleased() {

    }

}
