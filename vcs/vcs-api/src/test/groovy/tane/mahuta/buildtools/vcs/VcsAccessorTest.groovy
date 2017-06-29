package tane.mahuta.buildtools.vcs

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 29.06.17.
 */
@Subject(VcsAccessor)
class VcsAccessorTest extends Specification {

    @Unroll
    def 'isOnBranch(#prefix) for #branch returns #expected'() {
        setup:
        final accessor = createAccessor(branch, Mock(VcsFlowConfig))
        expect:
        accessor.isOnBranch(prefix) == expected

        where:
        branch | prefix | expected
        'xy'   | 'z/'   | false
        'xy'   | 'x'    | true
    }

    @Unroll
    def 'removeBranchPrefix(#prefix) for #branch returns #expected'() {
        setup:
        final accessor = createAccessor(branch, Mock(VcsFlowConfig))
        expect:
        accessor.removeBranchPrefix(prefix) == expected

        where:
        branch | prefix | expected
        'xy'   | 'z/'   | null
        'xy'   | 'x'    | 'y'
    }

    private VcsAccessor createAccessor(final String forBranch, final VcsFlowConfig flowConfig) {
        new VcsAccessor() {

            String getBranch() { forBranch }
            final String revisionId = "xy"
            final Collection<String> uncommittedFilePaths = []

            VcsFlowConfig getFlowConfig() { flowConfig }

            @Override
            void startReleaseBranch(@Nonnull String version) {}

            @Override
            boolean finishReleaseBranch(@Nonnull String version) { false }

            @Override
            void commitFiles(@Nonnull String message) {}

            @Override
            void push() {}

            @Override
            void checkout(@Nonnull String branch) {}
        }
    }

}
