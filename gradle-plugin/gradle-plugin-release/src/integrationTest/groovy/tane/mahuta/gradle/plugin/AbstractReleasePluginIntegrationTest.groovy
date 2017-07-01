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
    TemporaryFolder remoteRepositoryDir = new TemporaryFolder()
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()

    protected def getGradleRunner() {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .withDebug(true)
    }

    protected def getGradleReleaseRunner() {
        gradleRunner.withArguments('release', '--stacktrace')
    }

    private Git remoteGit, git

    def setup() {
        remoteGit = Git.init().setBare(true).setDirectory(remoteRepositoryDir.root).call()
        git = Git.cloneRepository().setDirectory(testProjectDir.root).setURI(remoteRepositoryDir.root.toURI().toASCIIString()).call()
        log.info("Created git repository at {}", testProjectDir.root)
        new File(testProjectDir.root, '.repository').mkdirs()
        new File(testProjectDir.root, ".gitignore") << """.gradle
build
.repository
"""
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

    boolean hasLocalTag(final String name) {
        git.repository.getRef("refs/tags/${name}") != null
    }

    boolean hasRemoteTag(final String name) {
        remoteGit.repository.getRef("refs/tags/${name}") != null
    }

    File getImplementationFile() {
        new File(testProjectDir.root, "impl/src/main/java/com/example/SomeImplementation.java")
    }

    File getApiFile() {
        new File(testProjectDir.root, "api/src/main/java/com/example/SomeInterface.java")
    }
}
