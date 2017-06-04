package tane.mahuta.gradle.plugins.version

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import tane.mahuta.gradle.plugins.version.persistence.VersionStorage

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@CompileStatic
@EqualsAndHashCode
class VersionExtension {

    private def rawVersion, parsedVersion

    private VersionStorage storage
    private VersionParser<?> parser = VersionParser.Factory.NOP

    /**
     * Set a new parser for the version.
     * @param parser the parser
     */
    VersionExtension setParser(@Nullable final VersionParser<?> parser) {
        this.parser = parser ?: VersionParser.Factory.NOP
        reparse()
        this
    }

    /**
     * Uses a closure for parsing.
     * @see VersionExtension#setParser(tane.mahuta.gradle.plugins.version.VersionParser)
     */
    VersionExtension setParser(@Nonnull
                               @ClosureParams(value = FromString, options = "java.lang.Object")
                               final Closure<?> parser) {
        this.parser = parser != null ?
                VersionParser.Factory.fromClosure(parser) :
                VersionParser.Factory.NOP
        reparse()
        this
    }

    VersionExtension setRawVersion(final rawVersion) {
        this.rawVersion = rawVersion
        reparse()
        this
    }

    VersionExtension setStorage(final VersionStorage storage) {
        this.storage = storage
        load()
        this
    }

    VersionExtension load() {
        if (storage != null) {
            setRawVersion(storage.load())
        }
        this
    }

    VersionExtension store() {
        storage?.store(parsedVersion ?: rawVersion)
        this
    }

    @Override
    boolean equals(final Object o) {
        if (o == null) {
            return (parsedVersion ?: rawVersion) == null
        }
        parsedVersion == o || rawVersion == o
    }

    @Override
    String toString() {
        (parsedVersion ?: rawVersion)?.toString() ?: "undefined"
    }

    private void reparse() {
        parsedVersion = this.parser.parse(rawVersion)
    }

}
