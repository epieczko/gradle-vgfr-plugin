package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport
import tane.mahuta.buildtools.semver.DefaultSemanticVersion
import tane.mahuta.buildtools.semver.DefaultSemanticVersionParser
import tane.mahuta.buildtools.version.SemanticVersion
import tane.mahuta.gradle.plugin.version.VersioningExtension

import javax.annotation.Nonnull

/**
 * Version plugin which provides mechanisms to load and store versions.
 * <p>
 *     Usage:
 *     <pre>
 *         apply plugin: 'tane.mahuta.gradle.semver-plugin'
 *     </pre>
 * </p>
 * <p>
 *     It uses {@link VersionPlugin} to load the version and transforms it by {@link DefaultSemanticVersion#parse(java.lang.String)}.
 * </p>
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
class SemanticVersionPlugin implements Plugin<ProjectInternal> {

    @Override
    void apply(@Nonnull final ProjectInternal target) {
        target.pluginManager.apply(VersionPlugin)
        final versionExtension = target.extensions.getByType(VersioningExtension)
        versionExtension.setParser(DefaultSemanticVersionParser.instance)
        versionExtension.setComparatorClosure({ v1, v2 -> v1 <=> v2 })
        versionExtension.setReleaseTransformerClosure(this.&transformToRelease.ncurry(1, target.projectDir))
        versionExtension.setReleaseTransformerForReportClosure(this.&transformReleaseForReport.ncurry(2, target.projectDir))
        versionExtension.setNextDevelopmentTransformerClosure(this.&transformNextDevelopment.ncurry(1, target.projectDir))
    }

    private SemanticVersion transformToRelease(@Nonnull final Object version, @Nonnull final File projectDir) {
        final SemanticVersion semVer = version instanceof SemanticVersion ? version as SemanticVersion : DefaultSemanticVersionParser.instance.parse(version as String, projectDir)
        new DefaultSemanticVersion(semVer.major, semVer.minor, semVer.micro, semVer.isSnapshot() ? null : semVer.qualifier)
    }

    private SemanticVersion transformNextDevelopment(@Nonnull final Object version, @Nonnull final File projectDir) {
        final SemanticVersion semVer = version instanceof SemanticVersion ? version as SemanticVersion : DefaultSemanticVersionParser.instance.parse(version as String, projectDir)
        new DefaultSemanticVersion(semVer.major, semVer.minor + 1, semVer.micro != null ? 0 : null, "SNAPSHOT")
    }

    @Nonnull
    private SemanticVersion transformReleaseForReport(
            @Nonnull final Object version,
            @Nonnull final ApiCompatibilityReport report, @Nonnull final File projectDir) {
        final SemanticVersion semVer = version instanceof SemanticVersion ? version as SemanticVersion : DefaultSemanticVersionParser.instance.parse(version as String, projectDir)
        int major = semVer.major, minor = semVer.minor
        Integer micro = semVer.micro
        if (!report.definiteIncompatibleClasses.isEmpty()) {
            major++
            minor = 0
            micro = micro != null ? 0 : null
        } else if (!report.possibleIncompatibleClasses.isEmpty()) {
            minor++
            micro = micro != null ? 0 : null
        } else if (micro != null) {
            micro++
        } else {
            minor++
        }
        new DefaultSemanticVersion(major, minor, micro, semVer.isSnapshot() ? null : semVer.qualifier)
    }

}
