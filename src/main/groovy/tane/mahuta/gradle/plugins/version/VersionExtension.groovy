package tane.mahuta.gradle.plugins.version

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import tane.mahuta.build.version.Version
import tane.mahuta.build.version.VersionParser
import tane.mahuta.build.version.VersionStorage
import tane.mahuta.build.version.VersionTransformer

import javax.annotation.Nullable
/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@CompileStatic
@EqualsAndHashCode
class VersionExtension {

    private def rawVersion
    private Version parsedVersion

    private final Map<String, VersionTransformer<? extends Version, ? extends Version>> transformers = [:]

    private VersionStorage storage

    private VersionParser<? extends Version> parser

    /**
     * Set a new parser for the version.
     * @param parser the parser
     */
    VersionExtension setParser(@Nullable final VersionParser<? extends Version> parser) {
        this.parser = parser
        reparse()
        this
    }

    /**
     * @return the parser for this extension
     */
    VersionParser<? extends Version> getParser() { parser }

    /**
     * Sets the raw version from the project.
     * This triggers parsing the version.
     * @param rawVersion the raw version from the project
     * @return ( this )
     */
    VersionExtension setRawVersion(final rawVersion) {
        this.rawVersion = rawVersion
        reparse()
        this
    }

    /**
     * Sets the {@link VersionStorage}.
     * This triggers a {@link VersionExtension#load()}.
     * @param storage the storage to be used
     * @return (this)
     */
    VersionExtension setStorage(@Nullable final VersionStorage storage) {
        this.storage = storage
        load()
        this
    }

    /**
     * Loads the version from the storage using {@link VersionStorage#load()}
     * and sets it using {@link VersionExtension#setRawVersion(java.lang.Object)}.
     * @return (this)
     */
    VersionExtension load() {
        if (storage != null) {
            setRawVersion(storage.load())
        }
        this
    }

    /**
     * Stores the version using {@link VersionStorage#store(java.lang.Object)}.
     * @return (this)
     */
    VersionExtension store() {
        storage?.store(parsedVersion?.toStorable() ?: rawVersion)
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
        parsedVersion = this.parser != null ? this.parser.parse(rawVersion) : null
    }
}
