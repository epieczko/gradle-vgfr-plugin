package tane.mahuta.buildtools.release;

import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder;
import tane.mahuta.buildtools.dependency.ArtifactResolver;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.version.VersionStorage;

import javax.annotation.Nonnull;

/**
 * Bundled services for the release of a single project.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public interface ReleaseInfrastructure {

    /**
     * @return the version handler to be used
     */
    @Nonnull
    VersionHandler<? super Object> getVersionHandler();

    /**
     * @return the version storage to be used
     */
    @Nonnull
    VersionStorage getVersionStorage();

    /**
     * @return the version control system accessor
     */
    @Nonnull
    VcsAccessor getVcs();

    /**
     * @return an dependency resolver
     */
    @Nonnull
    ArtifactResolver getArtifactResolver();

    /**
     * @return the factory for the compatibility report builder for the API
     */
    @Nonnull
    ApiCompatibilityReportBuilder.Factory getApiCompatibilityReportBuilderFactory();

}
