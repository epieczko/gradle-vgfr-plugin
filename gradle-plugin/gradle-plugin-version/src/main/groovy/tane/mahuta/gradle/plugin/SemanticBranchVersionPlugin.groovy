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
        versionExtension.setReleaseTransformerClosure(this.&transformToRelease.curry(versionExtension.releaseTransformer).ncurry(1, target.projectDir))
        versionExtension.setReleaseTransformerForReportClosure(this.&transformReleaseForReport.curry(versionExtension.releaseTransformerForReport).ncurry(2, target.projectDir))
        versionExtension.setNextDevelopmentTransformer(this.&transformToNextDevelopmentVersion.curry(versionExtension.nextDevelopmentTransformer).ncurry(1, target.projectDir))
    }

    @Nonnull
    private SemanticBranchVersion transformToRelease(
            @Nonnull final Function<SemanticVersion, SemanticVersion> original,
            @Nonnull final Object version, @Nonnull final File projectDir) {
        final SemanticBranchVersion semBranchVer = version instanceof SemanticBranchVersion ? version as SemanticVersion : DefaultSemanticBranchVersionParser.instance.parse(version as String, projectDir)
        final SemanticVersion transformed = original.apply(semBranchVer)
        new DefaultSemanticBranchVersion(transformed.major, transformed.minor, transformed.micro, semBranchVer.&getBranchQualifier, transformed.qualifier)
    }

    @Nonnull
    private SemanticBranchVersion transformReleaseForReport(
            @Nonnull final BiFunction<SemanticVersion, ApiCompatibilityReport, SemanticVersion> original,
            @Nonnull final Object version,
            @Nonnull final ApiCompatibilityReport report, @Nonnull final File projectDir) {
        final SemanticBranchVersion semBranchVer = version instanceof SemanticBranchVersion ? version as SemanticVersion : DefaultSemanticBranchVersionParser.instance.parse(version as String, projectDir)
        final SemanticVersion transformed = original.apply(semBranchVer, report)
        new DefaultSemanticBranchVersion(transformed.major, transformed.minor, transformed.micro, semBranchVer.&getBranchQualifier, transformed.qualifier)
    }

    @Nonnull
    private SemanticBranchVersion transformToNextDevelopmentVersion(
            @Nonnull final Function<SemanticVersion, SemanticVersion> original,
            @Nonnull final Object version, @Nonnull final File projectDir) {
        final SemanticBranchVersion semBranchVer = version instanceof SemanticBranchVersion ? version as SemanticVersion : DefaultSemanticBranchVersionParser.instance.parse(version as String, projectDir)
        final SemanticVersion transformed = original.apply(semBranchVer)
        new DefaultSemanticBranchVersion(transformed.major, transformed.minor, transformed.micro, semBranchVer.&getBranchQualifier, transformed.qualifier)
    }

}
