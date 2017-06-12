package tane.mahuta.gradle.plugin.version

import groovy.transform.CompileStatic
import lombok.EqualsAndHashCode
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionParser
import tane.mahuta.buildtools.version.VersionStorage

import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@CompileStatic
@EqualsAndHashCode
class VersionExtension implements Comparable<Object> {

    private def storageVersion
    private Version parsedVersion

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
        this.storageVersion = rawVersion
        reparse()
        this
    }

    /**
     * Sets the {@link VersionStorage}.
     * This triggers a {@link VersionExtension#load()}.
     * @param storage the storage to be used
     * @return ( this )
     */
    VersionExtension setStorage(@Nullable final VersionStorage storage) {
        this.storage = storage
        load()
        this
    }

    /**
     * Loads the version from the storage using {@link VersionStorage#load()}
     * and sets it using {@link VersionExtension#setRawVersion(java.lang.Object)}.
     * @return ( this )
     */
    VersionExtension load() {
        if (storage != null) {
            setRawVersion(storage.load())
        }
        this
    }

    /**
     * Stores the version using {@link VersionStorage#store(java.lang.Object)}.
     * @return ( this )
     */
    VersionExtension store() {
        storage?.store(parsedVersion?.toStorable() ?: storageVersion)
        this
    }

    @Override
    String toString() {
        internalVersion as String ?: "undefined"
    }

    @Override
    int compareTo(final Object o) {
        if (o instanceof Comparable && internalVersion instanceof Comparable) {
            return (internalVersion as Comparable) <=> (o as Comparable)
        }
        return (internalVersion as String) <=> (o as String)
    }

    def propertyMissing(final String name) {
        if (internalVersion == null) {
            return null
        }
        internalVersion[name]
    }

    def propertyMissing(final String name, final def arg) {
        if (internalVersion == null) {
            throw new MissingPropertyException("No version set", name, getClass())
        }
        internalVersion[name] = arg
    }

    def methodMissing(final String name, final def args) {
        if (!internalVersion) {
            throw new MissingMethodException(name, getClass(), args as Object[])
        }
        internalVersion.invokeMethod(name, args)
    }

    private Object getInternalVersion() {
        (parsedVersion ?: storageVersion)
    }

    private void reparse() {
        if (storageVersion instanceof Version) {
            parsedVersion = storageVersion as Version
        } else {
            parsedVersion = this.parser != null ? this.parser.parse(storageVersion) : null
        }
        storageVersion = parsedVersion?.toStorable() ?: storageVersion
    }
}
