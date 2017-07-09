package tane.mahuta.gradle.plugin

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildSuccess
import spock.lang.PendingFeature

/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
class ReleasePluginIntegrationTest extends AbstractReleasePluginIntegrationTest {

    def 'creates release'() {
        when:
        final result = buildRelease()

        then: 'the necessary tasks have been run'
        result.task(':api:check').outcome == TaskOutcome.UP_TO_DATE // No tests, but the task should be run
        result.task(':impl:check').outcome == TaskOutcome.UP_TO_DATE // No tests, but the task should be run
        result.task(':release').outcome == TaskOutcome.SUCCESS // The release was successful

        and: 'the tags and branches are created'
        hasLocalTag('version/1.0.0')
        !hasLocalBranch("release/1.0.0")
        hasRemoteTag('version/1.0.0')
        !hasRemoteBranch("release/1.0.0")

        and: 'the local repository contains the artifacts'
        repositoryContains("test.group", "api", "1.0.0")
        repositoryContains("test.group", "impl", "1.0.0")

        and: 'the version in the gradle.properties has been set'
        gradleProperties.version == '1.1.0-SNAPSHOT'
    }

    def 'creates following release'() {
        setup: 'building the first release'
        buildRelease()

        when: 'building the next release'
        final result = buildRelease()

        then: 'the necessary tasks have been run'
        result.task(':api:check').outcome == TaskOutcome.UP_TO_DATE // No tests, but the task should be run
        result.task(':impl:check').outcome == TaskOutcome.UP_TO_DATE // No tests, but the task should be run
        result.task(':release').outcome == TaskOutcome.SUCCESS // The release was successful

        and: 'the tags and branches are created'
        hasLocalTag('version/1.1.0')
        !hasLocalBranch("release/1.1.0")
        hasRemoteTag('version/1.1.0')
        !hasRemoteBranch("release/1.1.0")

        and: 'the local repository contains the artifacts'
        repositoryContains("test.group", "api", "1.0.0")
        repositoryContains("test.group", "impl", "1.0.0")

        and: 'the version in the gradle.properties has been set'
        gradleProperties.version == '1.2.0-SNAPSHOT'
    }

    @PendingFeature(exceptions=UnexpectedBuildSuccess.class)
    def 'release fails when changing API'() {
        setup: 'creating the first release'
        buildRelease()
        and: 'modifying the API and implementation to become incompatible'
        implementationFile.text = implementationFile.text.replace('String s', 'int s')
        apiFile.text = apiFile.text.replace('String s', 'int s')
        git.commit().setAll(true).setMessage("Changing the API").call()

        when: 'running the next release'
        final result = gradleReleaseRunner.buildAndFail()

        then: 'the release check will fail, and no release is being run'
        result.task(':releaseCheck')?.outcome == TaskOutcome.FAILED
        result.task(':release') == null // Not run
    }

    def 'release fails when already released'() {
        setup: 'creating the first release'
        buildRelease()
        and: 'setting the old version'
        gradlePropertiesFile.text = gradlePropertiesFile.text.replaceAll('1.1.0', '1.0.0')
        git.commit().setAll(true).setMessage("Using the aready released version").call()

        when: 'running the next release'
        final result = gradleReleaseRunner.buildAndFail()

        then: 'the release check will fail, and no release is being run'
        result.task(':releaseCheck')?.outcome == TaskOutcome.FAILED
        result.task(':release') == null // Not run
    }

    protected BuildResult buildRelease() {
        gradleReleaseRunner.build()
    }
}
