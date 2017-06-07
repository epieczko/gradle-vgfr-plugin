package tane.mahuta.gradle.plugin

import org.gradle.api.InvalidUserCodeException
import org.gradle.api.Plugin
import org.gradle.api.Project
import tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory
import tane.mahuta.gradle.plugin.vcs.VcsExtension

import javax.annotation.Nonnull
/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class VcsPlugin implements Plugin<Project> {

    @Override
    void apply(@Nonnull final Project target) {
        final factory = ServiceLoaderProjectServiceFactory.getInstance(VcsAccessorFactory)
        final vcsAccessor = factory.create(target)
        if (vcsAccessor == null) {
            throw new InvalidUserCodeException("Cannot find git repository for: {}", target)
        }
        target.extensions.add("vcs", new VcsExtension(vcsAccessor))
    }

}
