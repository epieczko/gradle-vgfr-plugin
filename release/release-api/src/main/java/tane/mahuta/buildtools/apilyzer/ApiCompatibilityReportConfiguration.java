package tane.mahuta.buildtools.apilyzer;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;

/**
 * The configuration of the builder for the {@link ApiCompatibilityReport}.
 *
 * @author christian.heike@icloud.com
 * Created on 12.07.17.
 */
@Builder
public class ApiCompatibilityReportConfiguration {

    /**
     * Use the provided source jar file (current version) for comparison.
     */
    @Getter(onMethod = @__(@Nonnull))
    @NonNull
    private final File current;

    /**
     * Use the provided target jar file (other version) for comparison.
     */
    @Getter(onMethod = @__(@Nonnull))
    @NonNull
    private final File baseline;

    /**
     * Use the provided scope for scanning.
     */
    @Getter(onMethod = @__(@Nullable))
    private final Scope scope;

    /**
     * Use the provided packages and subpackages for scanning.
     */
    @Getter(onMethod = @__(@Nullable))
    private final Collection<String> includePackages;

    /**
     * Use the provided classes for scanning.
     */
    @Getter(onMethod = @__(@Nullable))
    private final Collection<String> includeClasses;

    /**
     * Use the provided classpath for the source (current) jar analysis.
     */
    @Getter(onMethod = @__(@Nullable))
    private final Collection<File> currentClasspath;

    /**
     * Use the provided classpath for the target (other) jar analysis.
     */
    @Getter(onMethod = @__(@Nullable))
    private final Collection<File> baselineClasspath;

    /**
     * Use the provided logger for output.
     */
    @Getter(onMethod = @__(@Nullable))
    private final Logger logger;

    /**
     * The report output type
     */
    @Getter(onMethod = @__(@Nullable))
    private final ReportOutputType reportOutputType;

    /**
     * The report output type
     */
    @Getter(onMethod = @__(@Nullable))
    private final File reportFile;

}
