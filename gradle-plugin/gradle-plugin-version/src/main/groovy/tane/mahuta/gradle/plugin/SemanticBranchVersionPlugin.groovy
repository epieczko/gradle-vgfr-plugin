package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport
import tane.mahuta.buildtools.semver.branch.DefaultSemanticBranchVersion
import tane.mahuta.buildtools.semver.branch.DefaultSemanticBranchVersionParser
import tane.mahuta.buildtools.version.SemanticBranchVersion
import tane.mahuta.buildtools.version.SemanticVersion
import tane.mahuta.gradle.plugin.version.VersioningExtension

import javax.annotation.Nonnull
import java.util.function.BiFunction
import java.util.function.Function

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

        final versionExtension = target.extensions.findByType(VersioningExtension)

        versionExtension.parser = DefaultSemanticBranchVersionParser.instance
        versionExtension.setReleaseTransformerClosure(this.&transformToRelease.curry(versionExtension.releaseTransformer))
        versionExtension.setReleaseTransformerForReportClosure(this.&transformReleaseForReport.curry(versionExtension.releaseTransformerForReport))

    }

    @Nonnull
    private SemanticBranchVersion transformToRelease(
            @Nonnull final Function<SemanticVersion, SemanticVersion> original,
            @Nonnull final SemanticBranchVersion v) {
        final SemanticVersion transformed = original.apply(v)
        new DefaultSemanticBranchVersion(transformed.major, transformed.minor, transformed.micro, v.&getBranchQualifier, transformed.qualifier)
    }

    @Nonnull
    private SemanticBranchVersion transformReleaseForReport(
            @Nonnull final BiFunction<SemanticVersion, ApiCompatibilityReport, SemanticVersion> original,
            @Nonnull final SemanticBranchVersion v, @Nonnull final ApiCompatibilityReport report) {
        final SemanticVersion transformed = original.apply(v, report)
        new DefaultSemanticBranchVersion(transformed.major, transformed.minor, transformed.micro, v.&getBranchQualifier, transformed.qualifier)
    }

}
