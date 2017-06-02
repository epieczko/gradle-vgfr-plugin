package tane.mahuta.gradle.plugins.version.persistence.impl

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
class AbstractPropertyVersionStorageFactoryTest extends Specification {

    @Rule
    final TemporaryFolder folderRule = new TemporaryFolder()

    private Project project, subproject


    private final AbstractPropertyVersionStorageFactory factory = new AbstractPropertyVersionStorageFactory() {

        @Override
        protected File propertyVersionFileIn(@Nonnull final File directory) {
            return new File(directory,"test.txt")
        }

        @Override
        protected String propertyName() {
            return "version"
        }
    }

    def setup() {
        project = ProjectBuilder.builder().withProjectDir(folderRule.root).build()
        subproject = ProjectBuilder.builder().withName("x").withProjectDir(new File(folderRule.root, "x")).withParent(project).build()
    }

    def 'create with no file present, does return null'() {
        expect:
        factory.create(subproject) == null
    }

    def 'create with file present returns version in file'() {
        setup:
        new File(project.projectDir, "test.txt").text = "version=My.Version\n"
        when:
        final storage = factory.create(subproject)
        then:
        storage != null
        and:
        storage.load() == "My.Version"
    }

}
