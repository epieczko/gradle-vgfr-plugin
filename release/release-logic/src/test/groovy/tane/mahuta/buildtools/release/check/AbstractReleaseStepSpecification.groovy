package tane.mahuta.buildtools.release.check

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder
import tane.mahuta.buildtools.dependency.ArtifactResolver
import tane.mahuta.buildtools.dependency.simple.DefaultGAVCDescriptor
import tane.mahuta.buildtools.release.*
import tane.mahuta.buildtools.release.reporting.ReleaseProblem
import tane.mahuta.buildtools.vcs.VcsAccessor
import tane.mahuta.buildtools.vcs.VcsFlowConfig
import tane.mahuta.buildtools.version.VersionStorage

/**
 * @author christian.heike@icloud.com
 * Created on 23.06.17.
 */
abstract class AbstractReleaseStepSpecification extends Specification {

    @Rule
    final TemporaryFolder folder = new TemporaryFolder()

    /**
     * the {@link VcsAccessor} mock
     */
    private final VcsAccessor vcs = Mock(VcsAccessor) {
        getFlowConfig() >> Mock(VcsFlowConfig) {
            getReleaseBranchPrefix() >> "release/"
            getProductionBranch() >> "master"
            getDevelopmentBranch() >> "develop"
            getHotfixBranchPrefix() >> "hotfix/"
            getFeatureBranchPrefix() >> "feature/"
            getSupportBranchPrefix() >> "support/"
            getVersionTagPrefix() >> "version/"
        }
    }
    /**
     * the {@link VersionStorage} mock
     */
    private final VersionStorage versionStorage = Mock(VersionStorage)

    /**
     * the {@link VersionHandler} mock
     */
    private final VersionHandler<?> versionHandler = Mock(VersionHandler)

    /**
     * the {@link ArtifactResolver} mock
     */
    private final ArtifactResolver artifactResolver = Mock(ArtifactResolver)

    /**
     * the {@link ApiCompatibilityReportBuilder.Factory} mock
     */
    private
    final ApiCompatibilityReportBuilder.Factory apiCompatibilityFactory = Mock(ApiCompatibilityReportBuilder.Factory)

    /**
     * the {@link BuildToolAdapter} mock
     */
    private final BuildToolAdapter buildToolAdapter = Mock(BuildToolAdapter)

    /**
     * the {@link ReleaseInfrastructure} bundling the mocks
     */
    protected final ReleaseInfrastructure infrastructure = DefaultReleaseInfrastructure.builder().vcs(vcs)
            .versionStorage(versionStorage)
            .versionHandler(versionHandler)
            .artifactResolver(artifactResolver)
            .apiCompatibilityReportBuilderFactory(apiCompatibilityFactory)
            .buildToolAdapter(buildToolAdapter)
            .build()

    /**
     * the {@link ArtifactRelease} mock
     */
    protected final ArtifactRelease artifactRelease = Mock(ArtifactRelease)

    def setup() {
        final sourceVcs = this.vcs
        this.vcs.isOnBranch(_) >> { sourceVcs.getBranch()?.startsWith(it[0]) ?: false }
        artifactRelease.localFile >> new File(folder.root, "my.jar")
        artifactRelease.projectDir >> folder.root
        artifactRelease.descriptor >> DefaultGAVCDescriptor.builder().group("my.test").artifact("my-artifact").version('1.0.0-SNAPSHOT').build()
        final problems = []
        artifactRelease.describeProblem(_) >> {
            final b = ReleaseProblem.builder()
            it[0].accept(b)
            problems << b.build()
        }
        artifactRelease.getProblems() >> problems
        this.versionHandler.toReleaseVersion(_) >> "1.0.0"
    }
}
