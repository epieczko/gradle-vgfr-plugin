package tane.mahuta.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
class ReleasePluginNormalReleaseTest extends AbstractReleasePluginIntegrationTest {

    def 'stub'() {
        when:
        final result = gradleRunner.withPluginClasspath().withArguments('release', '--stacktrace').build()
        then:
        result.task(':release').outcome == TaskOutcome.SUCCESS // The release was successful
        and:
        hasLocalTag('version/1.0.0')
        and:
        hasRemoteTag('version/1.0.0')
    }
}
