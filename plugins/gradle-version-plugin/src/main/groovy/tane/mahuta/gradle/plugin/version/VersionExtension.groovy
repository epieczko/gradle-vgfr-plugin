package tane.mahuta.gradle.plugin.version

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import tane.mahuta.buildtools.version.Version
import tane.mahuta.buildtools.version.VersionParser
import tane.mahuta.buildtools.version.VersionStorage
import tane.mahuta.buildtools.version.VersionTransformer

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 04.06.17.
 */
@CompileStatic
@EqualsAndHashCode
class VersionExtension {

    private def storageVersion
    private Version parsedVersion

    /**
     * Named transformers to be invoked
     */
    private final Map<String, VersionTransformer> transformers = [:]

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
    boolean equals(final Object o) {
        parsedVersion == o || storageVersion == o
    }

    @Override
    String toString() {
        (parsedVersion ?: storageVersion)?.toString() ?: "undefined"
    }

    private void reparse() {
        if (storageVersion instanceof Version) {
            parsedVersion = storageVersion as Version
        } else {
            parsedVersion = this.parser != null ? this.parser.parse(storageVersion) : null
        }
        storageVersion = parsedVersion?.toStorable() ?: storageVersion
    }

    def propertyMissing(final String name) {
        final transformer = transformers[name]
        if (!transformer) {
            throw new MissingPropertyException("Could not find property or transformer with name: ${name}")
        }
        transformer
    }

    def propertyMissing(final String name, final def arg) {
        def args = (arg instanceof Object[] ? (Object[]) arg : [arg].findAll{ it != null }) as List<Object>
        if (args.size() != 1) {
            throw new IllegalArgumentException("Cannot add transformer with no implementation.")
        }
        def transformer = toVersionTransformer(args[0])
        if (transformer == null) {
            throw new IllegalArgumentException("Cannot create transformer from arguments: ${args}")
        }
        transformers[name] = transformer
        transformer
    }

    def methodMissing(final String name, final def args) {
        final transformer = transformers[name]
        final Object[] argArr = args instanceof Object[] ? args as Object[] : [args].toArray()
        if (!transformer) {
            throw new MissingMethodException(name, getClass(), argArr)
        }
        setRawVersion(transformer.transform(parsedVersion ?: storageVersion, argArr))
    }

    @Nullable
    private static VersionTransformer toVersionTransformer(
            @Nonnull final Object o) {
        if (o instanceof VersionTransformer) {
            return o as VersionTransformer
        }
        if (o instanceof Closure) {
            final Closure c = o as Closure
            if (c.getParameterTypes().length > 0) {
                return VersionTransformerFactory.create(c)
            }
        }
        null
    }
}
