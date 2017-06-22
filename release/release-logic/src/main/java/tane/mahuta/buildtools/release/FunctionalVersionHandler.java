package tane.mahuta.buildtools.release;

import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.Delegate;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReport;
import tane.mahuta.buildtools.version.VersionParser;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implementation of the {@link VersionHandler} using functional interfaces and a builder pattern.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
@Builder
public class FunctionalVersionHandler<V> implements VersionHandler<V> {

    @NonNull
    private final BiFunction<V, ApiCompatibilityReport, V> toReleaseVersionWithReportHandler;
    @NonNull
    private final Function<V, V> toReleaseVersionHandler;
    @NonNull
    @Delegate
    private final VersionParser<V> parser;

    @Nonnull
    @Override
    public V toReleaseVersionWithReport(@Nonnull final V version, @Nonnull final ApiCompatibilityReport apiReport) {
        return toReleaseVersionWithReportHandler.apply(version, apiReport);
    }

    @Nonnull
    @Override
    public V toReleaseVersion(@Nonnull V version) {
        return toReleaseVersionHandler.apply(version);
    }
}
