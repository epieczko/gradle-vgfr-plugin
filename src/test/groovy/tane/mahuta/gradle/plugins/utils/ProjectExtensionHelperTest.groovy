package tane.mahuta.gradle.plugins.utils

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject

/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@Subject(ProjectExtensionHelper)
class ProjectExtensionHelperTest extends Specification {

    private final Project project = ProjectBuilder.builder().withName("platsch").build()

    def 'creates extension lazily and adds it'() {
        setup:
        final extHelper = new ProjectExtensionHelper<String>("blubb", { it.name })

        when:
        final ext = extHelper.getOrCreate(project)
        then:
        ext == project.name
        and:
        project.extensions.findByName("blubb") == project.name
    }

    def 'does not store null factory results'() {
        setup:
        final extHelper = new ProjectExtensionHelper<String>("blubb", { null })

        when:
        final ext = extHelper.getOrCreate(project)
        then:
        ext == null
        and:
        project.extensions.findByName("blubb") == null

    }

}
