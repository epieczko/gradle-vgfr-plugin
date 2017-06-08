package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
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
