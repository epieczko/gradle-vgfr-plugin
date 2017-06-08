package tane.mahuta.gradle.plugin

import groovy.transform.CompileStatic
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import tane.mahuta.buildtools.version.DefaultSemanticVersion
import tane.mahuta.buildtools.version.SemanticVersion
import tane.mahuta.gradle.plugin.version.TransformerName
import tane.mahuta.gradle.plugin.version.VersionExtension
import tane.mahuta.gradle.plugin.version.VersionParserFactory
import tane.mahuta.gradle.plugin.version.transform.ChangeLevel

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
        final versionExtension = target.version as VersionExtension
        versionExtension.setParser(VersionParserFactory.create {
            DefaultSemanticVersion.parse(it as String)
        })
        versionExtension.defineTransformer(TransformerName.TO_RELEASE, { v ->
            DefaultGroovyMethods.with(DefaultSemanticVersion.parse(v as String), {
                DefaultSemanticVersion.of(it.major, it.minor, it.micro, null)
            })
        })
        versionExtension.defineTransformer(TransformerName.TO_NEXT_SNAPSHOT, this.&nextSnapshot)
    }

    protected SemanticVersion nextSnapshot(@Nonnull final SemanticVersion source, final ChangeLevel l) {
        final ChangeLevel changeLevel = Optional.ofNullable(l).orElse(ChangeLevel.API_EXTENSION)
        int major = source.major, minor = source.minor
        Integer micro = source.micro
        switch (changeLevel) {
            case ChangeLevel.API_INCOMPATIBILITY:
                major++
                break
            case ChangeLevel.API_EXTENSION:
                minor++
                break
            case ChangeLevel.IMPLEMENTATION_CHANGED:
                if (micro != null) {
                    micro++
                }
                break
        }
        DefaultSemanticVersion.of(major, minor, micro, "SNAPSHOT")
    }

}
