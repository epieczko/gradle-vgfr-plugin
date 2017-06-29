package tane.mahuta.gradle.plugin

import groovy.util.logging.Slf4j
import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@Slf4j
abstract class AbstractReleasePluginIntegrationTest extends Specification {

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()

    protected def getGradleRunner() {
        GradleRunner.create().withProjectDir(testProjectDir.root).withDebug(true)
    }

    private Git git

    def setup() {
        git = Git.init().setDirectory(testProjectDir.root).call()
        log.info("Created git repository at {}", testProjectDir.root)
        new File(testProjectDir.root, "readme.md") << "* Just a test"
        commit("Initial commit")
        git.checkout().setCreateBranch(true).setName("develop").call()
        log.info("Checked out development branch.")
        final source = getResourceFile("/baseProject/build.gradle").parentFile
        FileUtils.copyDirectory(source, testProjectDir.root)
        new File(testProjectDir.root, "gradle.properties") << "integrationTest=true\n"
        commit("Initial project")
    }

    def commit(final String msg) {
        git.add().addFilepattern('.').call()
        def ref = git.commit().setAll(true).setMessage(msg).call()
        log.info("Created commit '{}': {}.", msg, ref.name())
        ref
    }

    protected File getResourceFile(final String path) {
        final url = getClass().getResource(path)
        if (url == null) {
            throw new IllegalArgumentException("Could not find resource with path: ${path}")
        }
        return new File(url.toURI())
    }


}
