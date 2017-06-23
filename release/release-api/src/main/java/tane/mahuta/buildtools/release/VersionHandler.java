package tane.mahuta.buildtools.release;

import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;
import tane.mahuta.buildtools.version.VersionParser;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * Interface for a handler which transforms for the different steps in the release process.
 *
 * @param <V> the version type
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public interface VersionHandler<V> extends VersionParser<V> {
    
    /**
     * Get the next release version for the provided version using the API report.
     *
     * @param version the version to be used
     * @param apiReport the api report which
     * @return the release version according to the report
     */
    @Nonnull
    V toReleaseVersionWithReport(@Nonnull V version, @Nonnull ApiCompatibilityReport apiReport);

    /**
     * Get the release version of the provided version.
     * @param version the version to be released
     * @return the release version
     */
    @Nonnull
    V toReleaseVersion(@Nonnull V version);

    /**
     * @return the version comparator
     */
    Comparator<V> getComparator();

}
