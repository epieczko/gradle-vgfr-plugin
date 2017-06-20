package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

/**
 * Aggregation task which reports the errors found for the projects.
 *
 * @author christian.heike@icloud.com
 * Created on 09.06.17.
 */
@CompileStatic
class ReleaseCheckReportTask extends AbstractReleaseExtensionTask {

    ReleaseCheckReportTask() {
        description = 'checks the preconditions for the release'
    }

    @TaskAction
    void errorProblems() {
        if (releaseExtension.problems.hasErrors()) {
            releaseExtension.problems.log(logger)
            throw new InvalidUserDataException("Cannot continue release, problems have been found.")
        }
    }

}
