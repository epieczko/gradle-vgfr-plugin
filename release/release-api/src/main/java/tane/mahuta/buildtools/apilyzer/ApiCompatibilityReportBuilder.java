package tane.mahuta.buildtools.apilyzer;

import javax.annotation.Nonnull;

/**
 * Reporter with builder pattern which creates an {@link ApiCompatibilityReport}.
 *
 * @author christian.heike@icloud.com
 * Created on 19.06.17.
 */
public interface ApiCompatibilityReportBuilder {

    /**
     * Builds the report and returns the result.
     *
     * @return the result of the analysis
     */
    ApiCompatibilityReport buildReport(@Nonnull ApiCompatibilityReportConfiguration config);

    /**
     * Factory interface.
     *
     * @param <S> the type of the report builder
     */
    interface Factory<S extends ApiCompatibilityReportBuilder> {

        /**
         * @return a new builder
         */
        S builder();

    }

}
