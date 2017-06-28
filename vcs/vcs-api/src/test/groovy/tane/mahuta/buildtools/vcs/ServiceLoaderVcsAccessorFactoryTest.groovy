package tane.mahuta.buildtools.vcs

import spock.lang.Specification

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 14.06.17.
 */
class ServiceLoaderVcsAccessorFactoryTest extends Specification {

    def "finds service definitions and returns mock"() {
        expect:
        ServiceLoaderVcsAccessorFactory.getInstance().create(new File('.')) != null
    }

    static class TestFactory implements VcsAccessorFactory {


        @Override
        VcsAccessor create(@Nonnull File directory) {
            new VcsAccessor() {
                @Override
                String getBranch() { null }

                @Override
                String getRevisionId() { null }

                @Override
                Collection<String> getUncommittedFilePaths() { [] }

                @Override
                VcsFlowConfig getFlowConfig() { null }

                @Override
                void startReleaseBranch(@Nonnull String version) {
                }

                @Override
                boolean finishReleaseBranch(@Nonnull String version) {
                    return false
                }

                @Override
                void commitFiles(@Nonnull String message) {

                }

                @Override
                void push() {

                }

                @Override
                void checkout(@Nonnull String branch) {

                }
            }
        }
    }

}
