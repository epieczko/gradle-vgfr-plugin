package tane.mahuta.gradle.plugins.version

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable

import javax.annotation.Nonnull
import java.util.regex.Pattern

/**
 * Semantic version object.
 * @author christian.heike@icloud.com
 * Created on 23.05.17.
 */
@Immutable
@EqualsAndHashCode
@CompileStatic
class SemanticVersion implements Comparable<SemanticVersion> {

    /**
     * The semantic versioning pattern including the qualifier
     */
    private static final Pattern PATTERN = Pattern.compile(/(\d+)\.(\d+)(\.\d+)?(-.+?)?/)

    /**
     * major, minor
     */
    final int major, minor

    /**
     * micro version part
     */
    final Integer micro

    /**
     * the qualifier from the version
     */
    final String qualifier

    private String stringRepresentation

    @Override
    String toString() {
        stringRepresentation = stringRepresentation ?: buildStringRepresentation()
    }

    @Nonnull
    String buildStringRepresentation() {
        final sb = new StringBuilder()
        sb.append(major)
        sb.append(".")
        sb.append(minor)
        if (micro) {
            sb.append(".")
            sb.append(micro)
        }
        if (qualifier) {
            sb.append("-")
            sb.append(qualifier)
        }
        sb.toString()
    }

    boolean isSnapshot() {
        qualifier == "SNAPSHOT"
    }

    /**
     * Parse the provided version string into a {@link SemanticVersion}.
     * @param version the string representation
     * @return the parsed version
     */
    static SemanticVersion parse(final String version) {
        if (version == null) {
            return null
        }
        final m = PATTERN.matcher(version)
        if (!m.matches()) {
            throw new IllegalArgumentException("Could not parse version string to semantic version: ${version}")
        }
        new SemanticVersion(major: m.group(1) as Integer,
                minor: m.group(2) as Integer,
                micro: m.group(3)?.substring(1) as Integer,
                qualifier: m.group(4)?.substring(1))
    }

    @Override
    int compareTo(final SemanticVersion o) {
        if (o == null) {
            return 1
        }
        final majorComp = major <=> o.major
        if (majorComp != 0) {
            return majorComp
        }
        final minorComp = minor <=> o.minor
        if (minorComp != 0) {
            return minorComp
        }
        final microComp = (micro ?: 0) <=> (o.micro ?: 0)
        if (microComp != 0) {
            return microComp
        }
        (isSnapshot() ? 0 : 1) <=> (o.isSnapshot() ? 0 : 1)
    }
}
