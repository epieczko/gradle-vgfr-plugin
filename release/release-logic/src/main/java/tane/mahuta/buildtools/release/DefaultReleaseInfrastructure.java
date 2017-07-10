package tane.mahuta.buildtools.release;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tane.mahuta.buildtools.apilyzer.ApiCompatibilityReportBuilder;
import tane.mahuta.buildtools.dependency.ArtifactResolver;
import tane.mahuta.buildtools.vcs.VcsAccessor;
import tane.mahuta.buildtools.version.VersionStorage;

import javax.annotation.Nonnull;

/**
 * Default implementation of {@link ReleaseInfrastructure}.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
@Builder
@RequiredArgsConstructor
public class DefaultReleaseInfrastructure implements ReleaseInfrastructure {

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final VersionHandler versionHandler;

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final VersionStorage versionStorage;

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final VcsAccessor vcs;

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final ArtifactResolver artifactResolver;

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final ApiCompatibilityReportBuilder.Factory apiCompatibilityReportBuilderFactory;

    @NonNull
    @Getter(onMethod = @__({@Override, @Nonnull}))
    private final BuildToolAdapter buildToolAdapter;

}
