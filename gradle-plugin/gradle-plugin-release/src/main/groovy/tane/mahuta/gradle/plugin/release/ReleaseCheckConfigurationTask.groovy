package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

import javax.annotation.Nonnull
/**
 * @author christian.heike@icloud.com
 * Created on 12.06.17.
 */
@CompileStatic
class ReleaseCheckConfigurationTask extends AbstractReleaseExtensionTask {

    @TaskAction
    void checkConfigurationCorrect() {
        project.allprojects.each(this.&checkVersionCanBeTransformed)
    }

    protected void checkVersionCanBeTransformed(@Nonnull final Project project) {
    }
}
