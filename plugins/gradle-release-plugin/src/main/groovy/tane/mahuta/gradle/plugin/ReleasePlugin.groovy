package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import tane.mahuta.gradle.plugin.release.ReleaseCheckReportTask
import tane.mahuta.gradle.plugin.release.ReleaseCheckSnapshotDependenciesTask
import tane.mahuta.gradle.plugin.release.ReleaseCheckUncommittedChangesTask
import tane.mahuta.gradle.plugin.release.ReleaseExtension

import javax.annotation.Nonnull
/**
 * A plugin which uses {@link VcsPlugin} and {@link VersionPlugin} for releasing.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
class ReleasePlugin implements Plugin<Project> {

    public static final String TASK_RELEASE_CHECK = "releaseCheck"

    @Override
    void apply(final Project target) {
        target.getPluginManager().apply(VersionPlugin)
        target.getPluginManager().apply(VcsPlugin)
        target.extensions.create(ReleaseExtension, target)

        target.task(TASK_RELEASE_CHECK, type: ReleaseCheckReportTask).dependsOn(
                taskWithNameConvention(target, ReleaseCheckUncommittedChangesTask),
                taskWithNameConvention(target, ReleaseCheckSnapshotDependenciesTask),
        )
    }

    static <T extends Task> T taskWithNameConvention(@Nonnull final Project p, @Nonnull final Class<T> taskClass) {
        final String name = taskClass.simpleName.uncapitalize().replaceAll(/Task$/, '')
        (T)p.task(name, type: taskClass)
    }

}
