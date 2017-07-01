package tane.mahuta.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
class ReleasePluginNormalReleaseTest extends AbstractReleasePluginIntegrationTest {

    def 'create release'() {
        when:
        def result = gradleReleaseRunner.build()
        then:
        result.task(':release').outcome == TaskOutcome.SUCCESS // The release was successful
        and:
        hasLocalTag('version/1.0.0')
        and:
        hasRemoteTag('version/1.0.0')

        when:
        implementationFile.text = implementationFile.text.replace('String s', 'int s')
        apiFile.text = apiFile.text.replace('String s', 'int s')
        and:
        result = gradleReleaseRunner.buildAndFail()
        then:
        result.task(':releaseCheck')?.outcome == TaskOutcome.FAILED
        result.task(':release') == null // Not run
    }
}
