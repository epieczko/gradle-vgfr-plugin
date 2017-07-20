package tane.mahuta.buildtools.apilyzer;

/**
 * Reporter with builder pattern which creates an {@link ApiCompatibilityReport}.
 *
 * @param <S> SELF for the fluent builder pattern
 * @author christian.heike@icloud.com
 *         Created on 19.06.17.
 */
public interface ApiCompatibilityReportBuilder<S extends ApiCompatibilityReportBuilder<S>> extends ApiCompatibilityReportConfiguration<S> {

    /**
     * Builds the report and returns the result.
     * @return the result of the analysis
     */
    ApiCompatibilityReport buildReport();

    /**
     * Factory interface.
     * @param <S> the type of the report builder
     */
    interface Factory<S extends ApiCompatibilityReportBuilder<S>> {

        /**
         * @return a new builder
         */
        ApiCompatibilityReportBuilder<S> builder();

    }

}
