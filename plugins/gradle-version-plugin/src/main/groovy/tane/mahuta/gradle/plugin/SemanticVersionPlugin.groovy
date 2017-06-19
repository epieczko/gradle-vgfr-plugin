package tane.mahuta.gradle.plugin

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.buildtools.version.DefaultSemanticVersion
import tane.mahuta.buildtools.version.DefaultSemanticVersionParser
import tane.mahuta.gradle.plugin.version.VersioningExtension
import tane.mahuta.gradle.plugin.version.VersionParserFactory

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
@CompileStatic
class SemanticVersionPlugin implements Plugin<ProjectInternal> {

    @Override
    void apply(@Nonnull final ProjectInternal target) {
        target.pluginManager.apply(VersionPlugin)
        final versionExtension = target.extensions.getByType(VersioningExtension)
        versionExtension.setParser(DefaultSemanticVersionParser.instance)
    }

}
