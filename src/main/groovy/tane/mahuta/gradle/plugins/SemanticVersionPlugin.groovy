package tane.mahuta.gradle.plugins

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.build.version.DefaultSemanticVersion
import tane.mahuta.build.version.VersionParserFactory
import tane.mahuta.gradle.plugins.version.VersionExtension

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
        (target.version as VersionExtension).setParser(VersionParserFactory.create {
            DefaultSemanticVersion.parse(it as String)
        })
    }

}
