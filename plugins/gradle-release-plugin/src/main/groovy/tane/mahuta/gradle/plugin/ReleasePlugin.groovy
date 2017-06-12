package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * A plugin which uses {@link VcsPlugin} and {@link VersionPlugin} for releasing.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
class ReleasePlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.getPluginManager().apply(VersionPlugin)
        target.getPluginManager().apply(VcsPlugin)

        target.task("releaseCheck", type: ReleaseCheck)
    }

}
