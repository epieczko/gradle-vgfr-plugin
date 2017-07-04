package tane.mahuta.gradle.plugin

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Requires

/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
class ReleasePluginIntegrationTest extends AbstractReleasePluginIntegrationTest {

    @Requires({ OnlineCheck.online })
    def 'creates release'() {
        when:
        final result = buildRelease()
        then:
        result.task(':release').outcome == TaskOutcome.SUCCESS // The release was successful
        and:
        hasLocalTag('version/1.0.0')
        !hasLocalBranch("release/1.0.0")
        hasRemoteTag('version/1.0.0')
        !hasRemoteBranch("release/1.0.0")

        and:
        repositoryContains("test.group", "api", "1.0.0")
        repositoryContains("test.group", "impl", "1.0.0")
        and:
        gradleProperties.version == '1.1.0-SNAPSHOT'
    }
/*
    def 'creates following release'() {
        setup:
        buildRelease()

        when:
        final result = buildRelease()
        then:
        result.task(':release').outcome == TaskOutcome.SUCCESS // The release was successful
        and:
        hasLocalTag('version/1.1.0')
        and:
        hasRemoteTag('version/1.1.0')
        and:
        repositoryContains("test.group", "api", "1.1.0")
        repositoryContains("test.group", "impl", "1.1.0")
    }

    def 'release fails when changing API'() {
        setup:
        buildRelease()
        implementationFile.text = implementationFile.text.replace('String s', 'int s')
        apiFile.text = apiFile.text.replace('String s', 'int s')
        git.commit().setAll(true).call()

        when:
        final result = gradleReleaseRunner.buildAndFail()
        then:
        result.task(':releaseCheck')?.outcome == TaskOutcome.FAILED
        result.task(':release') == null // Not run
    }
*/

    protected BuildResult buildRelease() {
        gradleReleaseRunner.build()
    }
}
