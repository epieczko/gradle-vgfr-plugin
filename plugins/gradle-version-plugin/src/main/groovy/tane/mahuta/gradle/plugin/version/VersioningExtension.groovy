package tane.mahuta.gradle.plugin.version

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import lombok.EqualsAndHashCode
import org.gradle.api.Project
import tane.mahuta.buildtools.version.VersionParser
import tane.mahuta.buildtools.version.VersionStorage

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@CompileStatic
@EqualsAndHashCode
class VersioningExtension {

    private final ProjectVersionAccessor versionAccessor
    private final File sourceDirectory

    /**
     * the storage to be used to store the version
     */
    VersionStorage storage
    /**
     * the parser to be used to parse the version
     */
    VersionParser<?> parser

    VersioningExtension(@Nonnull final Project project) {
        this.versionAccessor = new ProjectVersionAccessor(project)
        this.sourceDirectory = project.projectDir
    }

    /**
     * Set the parser. In case the parser is not {@code null}, the version is being parsed.
     * @param parser the parser to set
     */
    void setParser(@Nullable final VersionParser<?> parser) {
        this.parser = parser
        reparse()
    }

    /**
     * Set the parser using a closure.
     * @param parser the parser to be set
     */
    void setParserClosure(@Nullable
                              @ClosureParams(value = FromString, options = ["java.lang.Object", "java.io.File"])
                              final Closure<?> parser) {
        setParser(parser != null ? VersionParserFactory.create(parser) : null)
    }

    /**
     * Set the version and parses it, if a
     * @param version
     */
    void setVersion(final Object version) {
        this.versionAccessor.set(version)
        reparse()
    }

    private void reparse() {
        final source = this.versionAccessor.get() as String
        if (this.parser != null && source != null) {
            this.versionAccessor.set(parser.parse(source, this.sourceDirectory))
        }
    }
}
