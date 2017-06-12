package tane.mahuta.gradle.plugin.release

import org.gradle.api.tasks.TaskAction

/**
 * A task which checks if there are uncommitted changes.
 *
 * @author christian.heike@icloud.com
 * Created on 09.06.17.
 */
class ReleaseCheckUncommittedChangesTask extends AbstractReleaseExtensionTask {

    ReleaseCheckUncommittedChangesTask() {
        description = 'finds all uncommitted changes for the project'
    }

    @TaskAction
    void checkUncommitedChanges() {
        final uncommittedFiles = releaseExtension.vcs.uncommittedFilePaths
        if (!uncommittedFiles.isEmpty()) {
            releaseExtension.addProblem(project, "There are uncommitted files: ${uncommittedFiles}")
        }
    }

}
