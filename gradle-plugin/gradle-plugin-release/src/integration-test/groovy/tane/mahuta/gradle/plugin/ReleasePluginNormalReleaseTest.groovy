package tane.mahuta.gradle.plugin
/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
class ReleasePluginNormalReleaseTest extends AbstractReleasePluginIntegrationTest {

    def 'stub'() {
        expect:
        true
        /*when:
        final result = gradleRunner.withPluginClasspath().withArguments('release', '--stacktrace', '--info').build()
        then:
        result.task(':release').outcome == SUCCESSFUL*/
    }
}
