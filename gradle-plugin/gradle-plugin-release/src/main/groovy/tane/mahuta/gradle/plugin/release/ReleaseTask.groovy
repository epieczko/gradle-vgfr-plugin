package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import org.gradle.api.InvalidUserCodeException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import tane.mahuta.buildtools.dependency.Artifact
import tane.mahuta.buildtools.dependency.DependencyContainer
import tane.mahuta.buildtools.dependency.GAVCDescriptor
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.buildtools.release.AbstractArtifactRelease
import tane.mahuta.buildtools.release.ArtifactRelease
import tane.mahuta.buildtools.release.ReleaseStep
import tane.mahuta.buildtools.release.flow.*
import tane.mahuta.buildtools.release.reporting.Severity

/**
 * Gradle task which invokes all necessary steps for the release.
 *
 * @author christian.heike@icloud.com
 * Created on 24.06.17.
 */
@CompileStatic
class ReleaseTask extends ReleaseExtensionTask {

    @Input
    List<? extends ReleaseStep> steps = [
            StartReleaseStep.instance,
            CommitReleaseVersionStep.instance,
            RunReleaseBuildStep.instance,
            FinishReleaseStep.instance,
            CommitNextDevelopmentVersionStep.instance
    ]

    ReleaseTask() {
        onlyIf {
            !(steps?.isEmpty())
        }
    }

    @TaskAction
    void runSteps() {
        final stepsCompleted = []
        final stepsToBeDone = []
        final extension = releaseExtensionOf(project)
        final artifactRelease = createStubArtifactRelease()
        final infrastructure = extension.infrastructure
        steps?.findAll{ it != null }?.each { ReleaseStep step ->
            if (artifactRelease.getProblems().isEmpty()) {
                logger.info("Running step: {}", step.description)
                step.apply(artifactRelease, infrastructure)
            }
            if (artifactRelease.getProblems().isEmpty()) {
                stepsCompleted << step.description
            } else {
                stepsToBeDone << step.description
            }
        }
        artifactRelease.problems.each(this.&logProblem)
        if (artifactRelease.problems.any { it.severity == Severity.PROBLEM }) {
            final newLine = System.getProperty("line.separator")
            final msg = new StringBuilder()
                    .append("There were problems releasing the project, the following tasks have been completed:").append(newLine)
                    .append(stepsCompleted.join(newLine)).append(newLine)
                    .append("However, the following steps have to be done manually:").append(newLine)
                    .append(stepsToBeDone.join(newLine)).toString()
            throw new InvalidUserCodeException(msg)
        }
    }

    protected ArtifactRelease createStubArtifactRelease() {
        new AbstractArtifactRelease() {
            @Override
            File getProjectDir() { project.projectDir }

            @Override
            Set<? extends DependencyContainer> getDependencyContainers() { [] as Set }

            @Override
            Set<? extends Artifact> getClasspathDependencies() { [] as Set }

            @Override
            GAVCDescriptor getDescriptor() {
                DefaultGAVCDescriptor.builder()
                        .group(project.group as String)
                        .artifact(project.name)
                        .version(project.version as String)
                        .build()
            }

            @Override
            File getLocalFile() { project.buildFile }

            @Override
            boolean isInternalArtifact() {
                true
            }
        }
    }

}
