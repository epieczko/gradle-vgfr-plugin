package tane.mahuta.gradle.plugins.version.persistence.impl

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import tane.mahuta.gradle.plugins.version.persistence.VersionStorage

import javax.annotation.Nonnull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
/**
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
@CompileStatic
@Slf4j
class PropertyVersionStorage implements VersionStorage {

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
            file.withWriter { props.store(it, "Modified by versions plugin at ${DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())}")}
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
        file.withReader{ result.load(it) }
        log.debug("Loaded properties from file {}: {}", file, result)
        result
    }

}
