package tane.mahuta.gradle.plugin.vcs

import tane.mahuta.buildtools.vcs.VcsAccessor
import tane.mahuta.gradle.plugin.ProjectServiceFactory

/**
 * Interface for a factory for {@link VcsAccessor}s.
 * @author christian.heike@icloud.com
 * Created on 06.06.17.
 */
interface VcsAccessorFactory extends ProjectServiceFactory<VcsAccessor> {
}