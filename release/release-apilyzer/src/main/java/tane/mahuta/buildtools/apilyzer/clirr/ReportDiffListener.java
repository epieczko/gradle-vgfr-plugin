package tane.mahuta.buildtools.apilyzer.clirr;

import lombok.Getter;
import net.sf.clirr.core.ApiDifference;
import net.sf.clirr.core.DiffListener;
import net.sf.clirr.core.MessageTranslator;
import net.sf.clirr.core.Severity;
import org.slf4j.Logger;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Implementation of {@link DiffListener} for creating a {@link ApiCompatibilityReport}.
 *
 * @author christian.heike@icloud.com
 *         Created on 19.06.17.
 */
public class ReportDiffListener implements DiffListener {

    private final Logger logger;
    private final MessageTranslator translator = new MessageTranslator();

    private ClirrApiCompatibilityReport reportInProgress;
    private ClirrApiCompatibilityReport finalizedReport;

    /**
     * Create a new listener with a {@link Logger}.
     *
     * @param logger the optional logger
     */
    public ReportDiffListener(@Nullable final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void start() {
        Optional.ofNullable(logger).ifPresent(l -> logger.info("Starting analysis..."));
        reportInProgress = new ClirrApiCompatibilityReport();
        finalizedReport = null;
    }

    @Override
    public void reportDiff(final ApiDifference difference) {
        Objects.requireNonNull(reportInProgress, "Reporting has not started yet.");
        Optional.ofNullable(logger).ifPresent(l -> getLoggerConsumer(l, difference.getMaximumSeverity()).accept(difference.getReport(translator)));
        Optional.ofNullable(targetMapFor(difference.getMaximumSeverity())).ifPresent(target -> target.add(difference.getAffectedClass()));
    }

    @Override
    public void stop() {
        Objects.requireNonNull(reportInProgress, "Reporting has not started yet.");
        Optional.ofNullable(logger).ifPresent(l -> l.info("Analysis finished."));
        this.finalizedReport = reportInProgress;
        this.reportInProgress = null;
    }

    /**
     * @return the finalized report
     */
    public ApiCompatibilityReport getReport() {
        Objects.requireNonNull(finalizedReport, "Reporting has not been finished yet.");
        return finalizedReport;
    }

    @Nonnull
    private static Consumer<String> getLoggerConsumer(@Nonnull final Logger logger, final Severity severity) {
        if (severity == Severity.INFO) {
            return logger::info;
        } else if (severity == Severity.WARNING) {
            return logger::warn;
        } else if (severity == Severity.ERROR) {
            return logger::error;
        } else {
            return logger::trace;
        }
    }

    @Nullable
    private Set<String> targetMapFor(@Nonnull final Severity severity) {
        if (severity == Severity.WARNING) {
            return this.reportInProgress.possibleIncompatibleClasses;
        } else if (severity == Severity.ERROR) {
            return this.reportInProgress.definiteIncompatibleClasses;
        } else {
            return null;
        }
    }

    /**
     * {@link ApiCompatibilityReport} to be created by this builder.
     *
     * @author christian.heike@icloud.com
     *         Created on 19.06.17.
     */
    protected static class ClirrApiCompatibilityReport implements ApiCompatibilityReport {

        @Getter(onMethod = @__({@Override, @Nonnull}))
        private final Set<String> possibleIncompatibleClasses = new LinkedHashSet<>();
        @Getter(onMethod = @__({@Override, @Nonnull}))
        private final Set<String> definiteIncompatibleClasses = new LinkedHashSet<>();

        @Override
        public boolean isCompatible() {
            return possibleIncompatibleClasses.isEmpty() && definiteIncompatibleClasses.isEmpty();
        }
    }
}