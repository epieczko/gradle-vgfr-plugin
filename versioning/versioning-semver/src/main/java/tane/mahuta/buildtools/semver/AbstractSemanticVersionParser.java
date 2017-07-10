package tane.mahuta.buildtools.semver;

import tane.mahuta.buildtools.version.SemanticVersion;
import tane.mahuta.buildtools.version.VersionParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract {@link VersionParser} for {@link SemanticVersion}.
 *
 * @param <T> the actual type of the semantic version to be parsed
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
public abstract class AbstractSemanticVersionParser<T extends SemanticVersion> implements VersionParser<T> {

    /**
     * The semantic versioning pattern including the qualifier
     */
    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?(-.+?)?");

    @Nonnull
    @Override
    public T parse(@Nonnull final String source, @Nullable final File sourceDirectory) {
        final Matcher m = PATTERN.matcher(source);
        if (!m.matches()) {
            throw new IllegalArgumentException("Could not parse version string to semantic version: " + source);
        }

        final int major = Integer.parseInt(m.group(1));
        final int minor = Integer.parseInt(m.group(2));
        final Integer micro = Optional.ofNullable(m.group(3)).map(s -> s.substring(1)).map(Integer::parseInt).orElse(null);
        final String qualifier = Optional.ofNullable(m.group(4)).map(s -> s.substring(1)).orElse(null);

        return doCreate(major, minor, micro, qualifier, sourceDirectory);
    }

    /**
     * Create the version for the provided parameters.
     * @param major the major version
     * @param minor the minor version
     * @param micro the micro version (optional)
     * @param qualifier the qualifier (optional)
     * @param sourceDirectory the source directory for the project
     * @return the version created
     */
    @Nonnull
    protected abstract T doCreate(int major, int minor, @Nullable Integer micro, @Nullable String qualifier, @Nullable File sourceDirectory);

}
