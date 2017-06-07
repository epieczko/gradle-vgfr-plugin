package tane.mahuta.gradle.plugin.version.storage

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import tane.mahuta.buildtools.version.VersionStorage

import javax.annotation.Nonnull
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * {@link VersionStorage} for property files.
 *
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
@CompileStatic
@Slf4j
class PropertyVersionStorage implements VersionStorage {

    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'V'HH:mmZ")

    private final File file
    private final String propertyName

    PropertyVersionStorage(@Nonnull final File file, @Nonnull final String propertyName) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("File ${file} is not a file.")
        }
        this.file = file
        this.propertyName = propertyName
    }

    @Override
    void store(final Object version) {
        def props = loadProperties()
        final value = version as String
        if (props[propertyName] != value) {
            props[propertyName] = value
            file.withWriter { props.store(it, "Modified by versions plugin at ${TIMESTAMP_FORMAT.format(new Date())}") }
            log.debug("Saved new version: {}", value)
        } else {
            log.debug("Version {} has not changed: {}", props[propertyName], version)
        }
    }

    @Override
    def load() {
        loadProperties()[propertyName]
    }

    private Properties loadProperties() {
        final result = new Properties()
        file.withReader { result.load(it) }
        log.debug("Loaded properties from file {}: {}", file, result)
        result
    }

}
