package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import tane.mahuta.buildtools.version.DefaultSemanticBranchVersion
import tane.mahuta.buildtools.version.SemanticBranchVersion
import tane.mahuta.buildtools.version.SemanticVersion
import tane.mahuta.gradle.plugin.vcs.VcsExtension
import tane.mahuta.gradle.plugin.version.TransformerName
import tane.mahuta.gradle.plugin.version.VersionExtension
import tane.mahuta.gradle.plugin.version.VersionParserFactory
import tane.mahuta.gradle.plugin.version.transform.ChangeLevel

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * {@link SemanticVersionPlugin} enhanced by the {@link VcsPlugin} which uses {@link tane.mahuta.buildtools.version.SemanticBranchVersion}s.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
class SemanticBranchVersionPlugin implements Plugin<Project> {

    @Override
    void apply(@Nonnull final Project target) {
        target.pluginManager.apply(VcsPlugin)
        target.pluginManager.apply(SemanticVersionPlugin)
        final versionExtension = target.version as VersionExtension
        final vcsExtension = target.extensions.findByType(VcsExtension) as VcsExtension
        versionExtension.setParser(
                VersionParserFactory.decorate(versionExtension.parser, {
                    toSemanticBranchVersion(it, vcsExtension)
                })
        )
        final toReleaseTransformer = versionExtension.getTransformer(TransformerName.TO_RELEASE)
        versionExtension.defineTransformer(TransformerName.TO_RELEASE, { v ->
            toSemanticBranchVersion(toReleaseTransformer.transform(v), branchNameToQualifier(vcsExtension.branch, vcsExtension.flowConfig))
        })
        final toNextSnapshotTransformer = versionExtension.getTransformer(TransformerName.TO_NEXT_SNAPSHOT)
        versionExtension.defineTransformer(TransformerName.TO_NEXT_SNAPSHOT, { v, ChangeLevel l ->
            toSemanticBranchVersion(toNextSnapshotTransformer.transform(v, l), branchNameToQualifier(vcsExtension.branch, vcsExtension.flowConfig))
        })
    }

    @Nullable
    private static SemanticBranchVersion toSemanticBranchVersion(@Nullable final SemanticVersion semanticVersion,
                                                                 @Nonnull final VcsExtension vcsExtension) {
        semanticVersion != null ?
                DefaultSemanticBranchVersion.of(semanticVersion, branchNameToQualifier(vcsExtension)?.replaceAll("[^a-zA-Z0-9]+", "_")) :
                null
    }

    private static String branchNameToQualifier(@Nonnull final VcsExtension vcsExtension) {
        final name = vcsExtension.branch
        final flowConfig = vcsExtension.flowConfig
        String result
        if ([flowConfig.releaseBranchPrefix, flowConfig.hotfixBranchPrefix].any(name.&startsWith) ||
                [flowConfig.productionBranch, flowConfig.developmentBranch].any(name.&equals)) {
            return null
        } else if (name.startsWith(flowConfig.supportBranchPrefix)) {
            return name.replace(flowConfig.supportBranchPrefix, "")
        } else if (name.startsWith(flowConfig.featureBranchPrefix)) {
            return name.replace(flowConfig.featureBranchPrefix, "")
        }
        null
    }

}
