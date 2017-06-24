package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction
import tane.mahuta.buildtools.release.reporting.Severity

/**
 * Aggregation task which reports the errors found for the projects.
 *
 * @author christian.heike@icloud.com
 * Created on 09.06.17.
 */
@CompileStatic
class ReleaseCheckReportTask extends ReleaseExtensionTask {

    ReleaseCheckReportTask() {
        description = 'checks the preconditions for the release'
    }

    @TaskAction
    void echoProblems() {
        def problemCount = 0
        project.allprojects.each{ p ->
            final extension = releaseExtensionOf(p)
            extension.artifactReleases.each{ artifactRelease ->
                artifactRelease.problems.each { problem ->
                    switch (problem.severity) {
                        case Severity.WARNING:
                            logger.warn(problem.messageFormat, problem.formatArguments)
                        case Severity.PROBLEM:
                            logger.error(problem.messageFormat, problem.formatArguments)
                            problemCount++
                    }
                }
            }
        }
        if (problemCount > 0) {
            throw new InvalidUserDataException("Found ${problemCount} for the release, please consult the log.")
        }
    }

}
