package tane.mahuta.gradle.plugin.vcs

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import spock.lang.Specification
import spock.lang.Subject
import tane.mahuta.gradle.plugin.ServiceLoaderProjectServiceFactory
import tane.mahuta.gradle.plugin.vcs.accessor.JGitFlowAccessorFactory

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
@Subject(VcsAccessorFactory)
class VcsAccessorFactoryTest extends Specification {

    private final Project project = Stub(Project) {
        getExtensions() >> Mock(ExtensionContainer)
    }

    def 'service loader finds default implementations'() {
        when:
        final factory = ServiceLoaderProjectServiceFactory.getInstance(project, VcsAccessorFactory)
        then:
        factory != null
        and:
        factory.factories.collect { it.class } == [JGitFlowAccessorFactory]
    }

}
