package tane.mahuta.gradle.plugin.release

import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

/**
 * Aggregation task which reports the errors found for the projects.
 *
 * @author christian.heike@icloud.com
 * Created on 09.06.17.
 */
class ReleaseCheckReportTask extends AbstractReleaseExtensionTask {

    ReleaseCheckReportTask() {
        description = 'checks the preconditions for the release'
    }

    @TaskAction
    void errorProblems() {
        if (releaseExtension.hasProblems()) {
            def countProblems = 0
            releaseExtension.eachProblem { project, messages ->
                logger.error("Problems on {}", project)
                messages.each {
                    logger.error(" - {}", it)
                    countProblems++
                }
            }
            throw new InvalidUserDataException("Cannot continue release, found {} problems.", countProblems)
        }
    }

}
