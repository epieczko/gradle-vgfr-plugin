package tane.mahuta.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import tane.mahuta.buildtools.version.DefaultSemanticBranchVersionParser
import tane.mahuta.gradle.plugin.version.VersioningExtension

import javax.annotation.Nonnull
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
    }

}
