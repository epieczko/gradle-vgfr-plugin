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
        versionExtension.setReleaseTransformerClosure(this.&transformToRelease)
        versionExtension.setReleaseTransformerForReportClosure(this.&transformReleaseForReport)
        versionExtension.setNextDevelopmentTransformerClosure(this.&transformNextDevelopment)
    }

    private SemanticVersion transformToRelease(@Nonnull final SemanticVersion v) {
        new DefaultSemanticVersion(v.major, v.minor, v.micro, v.isSnapshot() ? null : v.qualifier)
    }

    private SemanticVersion transformNextDevelopment(@Nonnull final SemanticVersion v) {
        new DefaultSemanticVersion(v.major, v.minor + 1, v.micro != 0 ? 0 : null, "SNAPSHOT")
    }

    @Nonnull
    private SemanticVersion transformReleaseForReport(
            @Nonnull final SemanticVersion v, @Nonnull final ApiCompatibilityReport report) {
        int major = v.major, minor = v.minor
        Integer micro = v.micro
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
        new DefaultSemanticVersion(major, minor, micro, v.isSnapshot() ? null : v.qualifier)
    }

}
