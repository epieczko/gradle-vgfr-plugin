package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.StartParameter
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.initialization.NestedBuildFactory
import tane.mahuta.buildtools.release.BuildToolAdapter
import tane.mahuta.gradle.plugin.version.VersioningExtension

import javax.annotation.Nonnull

/**
 * The gradle implementation for {@link BuildToolAdapter}.
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@CompileStatic
class GradleBuildAdapter implements BuildToolAdapter {

    private final ProjectInternal project

    GradleBuildAdapter(@Nonnull final ProjectInternal project) {
        this.project = project
    }

    @Override
    void setVersion(@Nonnull final Object version) {
        project.extensions.findByType(VersioningExtension).setVersion(version)
    }

    @Override
    Object getVersion() {
        project.version
    }

    @Override
    boolean buildRelease() {
        final releaseExtension = project.extensions.findByType(ReleaseExtension)
        final tasks = (releaseExtension.releaseTasks ?: ['check', 'uploadArchives']) as LinkedHashSet
        final buildFactory = project.services.get(NestedBuildFactory)
        final startParameter = project.services.get(StartParameter.class).newBuild()
        startParameter.setCurrentDir(project.projectDir)
        startParameter.setProjectDir(project.projectDir)
        startParameter.setTaskNames(tasks)
        final build = buildFactory.nestedInstanceWithNewSession(startParameter)
        try {
            build.run()
        } finally {
            build.stop()
        }
    }
}
