package tane.mahuta.gradle.plugin.vcs

import spock.lang.Specification
import tane.mahuta.gradle.plugin.ServiceLoaderProjectServiceFactory
import tane.mahuta.gradle.plugin.vcs.accessor.JGitFlowAccessorFactory

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class VcsAccessorFactoryTest extends Specification {

    def 'service loader finds default implementations'() {
        expect:
        ServiceLoaderProjectServiceFactory.getInstance(VcsAccessorFactory).factories.collect {
            it.class
        } == [JGitFlowAccessorFactory]
    }

}
