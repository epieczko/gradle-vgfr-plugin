package tane.mahuta.buildtools.dependency;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Group, dependency, version and classifier descriptor.
 *
 * @author christian.heike@icloud.com
 *         Created on 22.06.17.
 */
public interface GAVCDescriptor {

    String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    char SEP = ':';

    /**
     * @return the group of the dependency
     */
    @Nonnull
    String getGroup();

    /**
     * @return the artifact name of the dependency
     */
    @Nonnull
    String getArtifact();

    /**
     * @return the artifact version of the dependency
     */
    @Nonnull
    String getVersion();

    /**
     * @return the optional classifier
     */
    String getClassifier();

    /**
     * @return {@code true} if the version is a SNAPSHOT version
     */
    default boolean isSnapshot() {
        return Optional.ofNullable(getVersion()).map(v -> v.endsWith(SNAPSHOT_SUFFIX)).orElse(false);
    }

    /**
     * @return a short descriptor in the form of g:a:v:c
     */
    default String toStringDescriptor() {

        final StringBuilder sb = new StringBuilder()
                .append(getGroup()).append(SEP)
                .append(getArtifact()).append(SEP)
                .append(getVersion());

        Optional.ofNullable(getClassifier()).ifPresent(c -> sb.append(SEP).append(c));

        return sb.toString();
    }
}
