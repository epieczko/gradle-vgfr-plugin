package tane.mahuta.buildtools.apilyzer.clirr;

import lombok.SneakyThrows;
import net.sf.clirr.core.Checker;
import net.sf.clirr.core.ClassSelector;
import net.sf.clirr.core.DiffListenerAdapter;
import net.sf.clirr.core.PlainDiffListener;
import net.sf.clirr.core.XmlDiffListener;
import net.sf.clirr.core.internal.bcel.BcelTypeArrayBuilder;
import net.sf.clirr.core.spi.JavaType;
import net.sf.clirr.core.spi.Scope;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportConfiguration;
import tane.mahuta.buildtools.apilyzer.ReportOutputType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

/**
 * CLIRR implementation of {@link ApiCompatibilityReportBuilder}.
 *
 * @author christian.heike@icloud.com
 * Created on 19.06.17.
 */
public class ClirrApiCompatibilityReportBuilder implements ApiCompatibilityReportBuilder {

    private static final Scope DEFAULT_SCOPE = Scope.PUBLIC;

    private final Checker checker = new Checker();

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

    @SneakyThrows
    @Nullable
    private DiffListenerAdapter createDiffListenerAdapter(@Nonnull final ApiCompatibilityReportConfiguration configuration) {
        final File file = configuration.getReportFile();
        final ReportOutputType type = configuration.getReportOutputType();
        if (type == null || file == null) {
            return null;
        }
        switch (type) {
            case XML:
                return new XmlDiffListener(file.getAbsolutePath());
            case TXT:
                return new PlainDiffListener(file.getAbsolutePath());
            default:
                throw new IllegalArgumentException("Cannot create a diff listener for output type: " + type);
        }
    }

    private <T> ClirrApiCompatibilityReportBuilder addAllIfNotnull(final Iterable<T> source, final Collection<T> target) {
        target.clear();
        Optional.ofNullable(source).map(Iterable::iterator).ifPresent(i -> i.forEachRemaining(b -> Optional.ofNullable(b).ifPresent(target::add)));
        return this;
    }

    @Nonnull
    private ClassSelector buildClassSelector(@Nonnull final ApiCompatibilityReportConfiguration config) {
        final Collection<String> includePackages = Optional.ofNullable(config.getIncludePackages()).orElseGet(Collections::emptyList);
        final Collection<String> includeClasses = Optional.ofNullable(config.getIncludeClasses()).orElseGet(Collections::emptyList);
        if (!includePackages.isEmpty() || !includeClasses.isEmpty()) {
            final ClassSelector selector = new ClassSelector(ClassSelector.MODE_IF);
            includePackages.forEach(selector::addPackageTree);
            includeClasses.forEach(selector::addClass);
            return selector;
        } else {
            return new ClassSelector(ClassSelector.MODE_UNLESS);
        }
    }

    @SneakyThrows
    private static URL[] filesToUrls(final Collection<File> source) {
        if (source == null) {
            return new URL[0];
        }
        final URL[] result = new URL[source.size()];
        final Iterator<File> iterator = source.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next().toURI().toURL();
        }
        return result;
    }

    @Override
    public ApiCompatibilityReport buildReport(@Nonnull final ApiCompatibilityReportConfiguration config) {
        final ClassLoader sourceLoader = new URLClassLoader(filesToUrls(config.getCurrentClasspath()));
        final ClassLoader targetLoader = new URLClassLoader(filesToUrls(config.getBaselineClasspath()));

        final ClassSelector classSelector = buildClassSelector(config);

        final JavaType[] sourceClasses = BcelTypeArrayBuilder.createClassSet(new File[]{config.getCurrent()}, sourceLoader, classSelector);
        final JavaType[] targetClasses = BcelTypeArrayBuilder.createClassSet(new File[]{config.getBaseline()}, targetLoader, classSelector);

        final ReportDiffListener listener = new ReportDiffListener(config.getLogger(), createDiffListenerAdapter(config));
        checker.getScopeSelector().setScope(Optional.ofNullable(config.getScope()).map(ClirrApiCompatibilityReportBuilder::mapScope).orElse(DEFAULT_SCOPE));
        checker.addDiffListener(listener);
        checker.reportDiffs(targetClasses, sourceClasses);
        return listener.getReport();
    }
}
