package tane.mahuta.buildtools.apilyzer;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The configuration of the builder for the {@link ApiCompatibilityReport}.
 *
 * @author christian.heike@icloud.com
 *         Created on 12.07.17.
 */
public interface ApiCompatibilityReportConfiguration<S extends ApiCompatibilityReportConfiguration<S>> {

    /**
     * Use the provided source jar file (current version) for comparison.
     *
     * @param source the source jar file
     * @return {@code this}
     */
    S withCurrent(@Nonnull File source);

    /**
     * Use the provided target jar file (other version) for comparison.
     *
     * @param target the source jar file
     * @return {@code this}
     */
    S withBaseline(@Nonnull File target);

    /**
     * Use the provided scope for scanning.
     *
     * @param scope the scanning scope (optional)
     * @return {@code this}
     */
    S withScope(@Nullable Scope scope);

    /**
     * Use the provided packages and subpackages for scanning.
     *
     * @param packageNames the names of the pakcages
     * @return {@code this}
     */
    S withPackages(@Nullable Iterable<String> packageNames);

    /**
     * Use the provided classes for scanning.
     *
     * @param classNames the names of the classes
     * @return {@code this}
     */
    S withClasses(Iterable<String> classNames);

    /**
     * Use the provided classpath for the source (current) jar analysis.
     *
     * @param sourceClasspath the jar files to be used
     * @return {@code this}
     */
    S withCurrentClasspath(@Nullable Iterable<File> sourceClasspath);

    /**
     * Use the provided classpath for the target (other) jar analysis.
     *
     * @param targetClasspath the jar files to be used
     * @return {@code this}
     */
    S withBaselineClasspath(@Nullable Iterable<File> targetClasspath);

    /**
     * Use the provided logger for output.
     *
     * @param logger the logger to be used
     * @return {@code this}
     */
    S withLogger(@Nullable Logger logger);

    /**
     * Create a report file.
     *
     * @param type the type of the output
     * @param file the file to output to
     * @return {@code this}
     */
    S withReportOutput(@Nonnull final ReportOutputType type, @Nonnull final File file);

    /**
     * @see #withReportOutput(ReportOutputType, File)
     */
    default S withReportOutput(@Nonnull final String type, @Nonnull final File file) {
        final ReportOutputType typeEnum = Arrays.stream(ReportOutputType.values())
                .filter(v -> v.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find type '" + type + "' in known types: " + String.join(", ", Arrays.stream(ReportOutputType.values()).map(Enum::name).collect(Collectors.toList()))));
        return withReportOutput(typeEnum, file);
    }
}
