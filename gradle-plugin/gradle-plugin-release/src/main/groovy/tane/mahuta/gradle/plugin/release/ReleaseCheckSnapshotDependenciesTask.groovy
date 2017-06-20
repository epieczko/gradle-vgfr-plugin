package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.TaskAction

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 12.06.17.
 */
@CompileStatic
class ReleaseCheckSnapshotDependenciesTask extends AbstractReleaseExtensionTask {

    @TaskAction
    void checkAllProjects() {
        project.allprojects.each(this.&checkConfigurations)
    }

    protected void checkConfigurations(final Project p) {
        final main = p.convention.findByType(JavaPluginConvention)?.sourceSets?.findByName("main")

        final configurations = [main?.compileConfigurationName, main?.compileOnlyConfigurationName].collect {
            it != null ? p.configurations.findByName(it) : null
        }.findAll { it != null } as LinkedHashSet<Configuration>


        for (int i = 0; i < configurations.size(); i++) {
            configurations.addAll(configurations[i].getExtendsFrom())
        }

        logger.debug 'Computed the following configurations to be scanned: {}', configurations

        configurations.each(this.&errorSnapshotDependencies.curry(p))
    }

    protected void errorSnapshotDependencies(@Nonnull final Project p, @Nonnull final Configuration c) {
        final snapshotDependencies = c.dependencies
                .findAll { Dependency d -> d?.version?.endsWith('SNAPSHOT') }
                .collect { d -> "${d.group}:${d.name}:${d.version}" }

        if (!(snapshotDependencies?.isEmpty())) {
            releaseExtension.problems.error(p, "configuration {} contains the following SNAPSHOT dependencies: {}", c, snapshotDependencies.join(', '))
        }
    }
}
