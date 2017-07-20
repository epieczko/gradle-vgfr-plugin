package tane.mahuta.buildtools.apilyzer.clirr;

import lombok.SneakyThrows;
import net.sf.clirr.core.*;
import net.sf.clirr.core.internal.bcel.BcelTypeArrayBuilder;
import net.sf.clirr.core.spi.JavaType;
import net.sf.clirr.core.spi.Scope;
import org.slf4j.Logger;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder;
import tane.mahuta.buildtools.apilyzer.ReportOutputType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * CLIRR implementation of {@link ApiCompatibilityReportBuilder}.
 *
 * @author christian.heike@icloud.com
 *         Created on 19.06.17.
 */
public class ClirrApiCompatibilityReportBuilder implements ApiCompatibilityReportBuilder<ClirrApiCompatibilityReportBuilder> {

    private static final Scope DEFAULT_SCOPE = Scope.PUBLIC;

    private final Checker checker = new Checker();

    private File current;
    private File baseline;

    private final Collection<String> includePackages = new HashSet<>();
    private final Collection<String> includeClasses = new HashSet<>();
    private final Collection<File> baselineCp = new HashSet<>();
    private final Collection<File> currentCp = new HashSet<>();
    private Logger logger;
    private DiffListener diffListener;

    @Override
    public ClirrApiCompatibilityReportBuilder withCurrent(final File source) {
        this.current = source;
        return this;
    }

    @Override
    public ClirrApiCompatibilityReportBuilder withBaseline(final File target) {
        this.baseline = target;
        return this;
    }

    @Override
    public ClirrApiCompatibilityReportBuilder withScope(@Nullable final tane.mahuta.buildtools.apilyzer.Scope scope) {
        checker.getScopeSelector().setScope(Optional.ofNullable(scope).map(ClirrApiCompatibilityReportBuilder::mapScope).orElse(DEFAULT_SCOPE));
        return this;
    }

    @Nonnull
    private static Scope mapScope(@Nonnull final tane.mahuta.buildtools.apilyzer.Scope scope) {
        switch (scope) {
            case PUBLIC:
                return Scope.PUBLIC;
            case PROTECTED:
                return Scope.PROTECTED;
            case PACKAGE:
                return Scope.PACKAGE;
            case PRIVATE:
                return Scope.PRIVATE;
            default:
                throw new IllegalArgumentException("Cannot map scope: " + scope);
        }
    }

    @Override
    public ClirrApiCompatibilityReportBuilder withPackages(final Iterable<String> packageNames) {
        return addAllIfNotnull(packageNames, this.includePackages);
    }

    @Override
    public ClirrApiCompatibilityReportBuilder withClasses(final Iterable<String> classNames) {
        return addAllIfNotnull(classNames, this.includeClasses);
    }

    @Override
    public ClirrApiCompatibilityReportBuilder withBaselineClasspath(final Iterable<File> sourceClasspath) {
        return addAllIfNotnull(sourceClasspath, this.baselineCp);
    }

    @Override
    public ClirrApiCompatibilityReportBuilder withLogger(final @Nullable Logger logger) {
        this.logger = logger;
        return this;
    }

    @Override
    @SneakyThrows
    public ClirrApiCompatibilityReportBuilder withReportOutput(@Nonnull final ReportOutputType type, @Nonnull final File file) {
        switch (type) {
            case XML:
                diffListener = new XmlDiffListener(file.getAbsolutePath());
                break;
            case TXT:
                diffListener = new PlainDiffListener(file.getAbsolutePath());
                break;
            default:
                throw new IllegalArgumentException("Cannot create a diff listener for output type: " + type);
        }
        return this;
    }

    @Override
    public ClirrApiCompatibilityReportBuilder withCurrentClasspath(final Iterable<File> sourceClasspath) {
        return addAllIfNotnull(sourceClasspath, this.currentCp);
    }

    private <T> ClirrApiCompatibilityReportBuilder addAllIfNotnull(final Iterable<T> source, final Collection<T> target) {
        target.clear();
        Optional.ofNullable(source).map(Iterable::iterator).ifPresent(i -> i.forEachRemaining(b -> Optional.ofNullable(b).ifPresent(target::add)));
        return this;
    }

    private void checkConfigured() {
        Objects.requireNonNull(current, "Source (current) jar file should be set.");
        Objects.requireNonNull(baseline, "Target (old) jar file should be set.");
    }

    @Nonnull
    private ClassSelector buildClassSelector() {
        if (!includePackages.isEmpty() || !includeClasses.isEmpty()) {
            final ClassSelector selector = new ClassSelector(ClassSelector.MODE_IF);
            includePackages.forEach(selector::addPackageTree);
            includeClasses.forEach(selector::addClass);
            return selector;
        } else {
            return new ClassSelector(ClassSelector.MODE_UNLESS);
        }
    }

    @Override
    public ApiCompatibilityReport buildReport() {
        checkConfigured();

        final ClassLoader sourceLoader = new URLClassLoader(filesToUrls(currentCp));
        final ClassLoader targetLoader = new URLClassLoader(filesToUrls(baselineCp));

        final ClassSelector classSelector = buildClassSelector();

        final JavaType[] sourceClasses = BcelTypeArrayBuilder.createClassSet(new File[]{current}, sourceLoader, classSelector);
        final JavaType[] targetClasses = BcelTypeArrayBuilder.createClassSet(new File[]{baseline}, targetLoader, classSelector);

        final ReportDiffListener listener = new ReportDiffListener(logger, diffListener);
        checker.addDiffListener(listener);
        checker.reportDiffs(targetClasses, sourceClasses);
        return listener.getReport();
    }

    @SneakyThrows
    private static URL[] filesToUrls(final Collection<File> source) {
        final URL[] result = new URL[source.size()];
        final Iterator<File> iterator = source.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next().toURI().toURL();
        }
        return result;
    }

}
