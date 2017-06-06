package tane.mahuta.gradle.plugins.vcs

import tane.mahuta.build.vcs.VcsAccessor
import tane.mahuta.gradle.plugins.ProjectServiceFactory

/**
 * Interface for a factory for {@link VcsAccessor}s.
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
interface VcsAccessorFactory extends ProjectServiceFactory<VcsAccessor> {
}