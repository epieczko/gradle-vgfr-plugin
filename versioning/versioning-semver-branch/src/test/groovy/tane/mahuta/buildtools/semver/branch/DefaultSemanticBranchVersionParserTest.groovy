package tane.mahuta.buildtools.semver.branch

import spock.lang.Specification
import spock.lang.Unroll
import tane.mahuta.buildtools.semver.branch.DefaultSemanticBranchVersionParser
import tane.mahuta.buildtools.vcs.VcsAccessor
import tane.mahuta.buildtools.vcs.VcsAccessorFactory
import tane.mahuta.buildtools.vcs.VcsFlowConfig

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 14.06.17.
 */
class DefaultSemanticBranchVersionParserTest extends Specification {

    private static ThreadLocal<VcsAccessorFactory> MOCK_TL = new ThreadLocal<>()

    @Unroll
    def 'parses #source in #branch correctly as #asString'() {
        setup:
        final accessorFactory = Stub(VcsAccessorFactory) {
            create(_) >> Stub(VcsAccessor) {
                getBranch() >> branch
                getFlowConfig() >> Stub(VcsFlowConfig) {
                    getFeatureBranchPrefix() >> "feature/"
                    getSupportBranchPrefix() >> "support/"
                    getHotfixBranchPrefix() >> "hotfix/"
                    getReleaseBranchPrefix() >> "release/"
                    getDevelopmentBranch() >> "development"
                    getProductionBranch() >> "master"
                }
            }
        }
        MOCK_TL.set(accessorFactory)

        when:
        final actual = DefaultSemanticBranchVersionParser.instance.parse(source, new File("."))
        then:
        actual.major == major
        and:
        actual.minor == minor
        and:
        actual.micro == micro
        and:
        actual.qualifier == qualifier
        and:
        actual.branchQualifier == branchQualifier
        and:
        actual as String


        where:
        source    | branch          | major | minor | micro | qualifier | branchQualifier | asString
        "1.2.3"   | null            | 1     | 2     | 3     | null      | null            | "1.2.3"
        "1.2.3"   | "fff"           | 1     | 2     | 3     | null      | "fff"           | "1.2.3-fff"
        "1.2.3"   | "master"        | 1     | 2     | 3     | null      | null            | "1.2.3"
        "1.2.3"   | "development"   | 1     | 2     | 3     | null      | null            | "1.2.3"
        "1.2.3"   | "release/1.2.3" | 1     | 2     | 3     | null      | null            | "1.2.3"
        "1.2.3"   | "hotfix/1.2.3"  | 1     | 2     | 3     | null      | null            | "1.2.3"
        "1.2.3"   | "feature/b"     | 1     | 2     | 3     | null      | "b"             | "1.2.3-b"
        "1.2.3-q" | null            | 1     | 2     | 3     | "q"       | null            | "1.2.3-q"
        "1.2.3-q" | "support/b"     | 1     | 2     | 3     | "q"       | "b"             | "1.2.3-b-q"

    }

    static class TestVcsFactory implements VcsAccessorFactory {

        @Override
        VcsAccessor create(@Nonnull File directory) {
            MOCK_TL.get().create(directory)
        }
    }

}
