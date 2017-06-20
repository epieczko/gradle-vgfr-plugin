package tane.mahuta.gradle.plugin.release

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.slf4j.Logger

import javax.annotation.Nonnull
import javax.annotation.Nullable
import java.util.function.BiConsumer

/**
 * Report which collects and logs warnings and errors for a project.
 *
 * @author christian.heike@icloud.com
 * Created on 12.06.17.
 */
@CompileStatic
class ProblemReport {

    private final Map<Project, Collection<Entry>> internalMap = [:]

    /**
     * Add a warning for the project.
     * @param project the project
     * @param format the message format
     * @param args the arguments for formatting
     */
    void warn(@Nonnull final Project project, @Nonnull final String format, @Nonnull final Object... args) {
        addEntry(project, new Entry(level: LogLevel.WARN, format: format, args: args))
    }

    /**
     * Add an error for the project.
     * @param project the project
     * @param format the message format
     * @param args the arguments for formatting
     */
    void error(@Nonnull final Project project, @Nonnull final String format, @Nonnull final Object... args) {
        addEntry(project, new Entry(level: LogLevel.WARN, format: format, args: args))
    }

    /**
     * @return true if errors have been collected, {@code false} otherwise
     */
    boolean hasErrors() {
        internalMap.values().any { entries -> entries.any { it.level == LogLevel.ERROR } }
    }

    /**
     * @return true if warnings have been collected, {@code false} otherwise
     */
    boolean hasWarnings() {
        internalMap.values().any { entries -> entries.any { it.level == LogLevel.WARN } }
    }

    /**
     * Log the contents of the report to the logger.
     * @param logger the logger to be used for logging
     */
    void log(final Logger logger) {
        internalMap.each { p, es ->
            es.groupBy { it.level }.each { l, entries ->
                final logImpl = loggerImplFor(logger, l)
                logImpl?.call('{}s for {}:', [l.name(), p].toArray())
                entries.each { logImpl?.call(it.format, it.args) }
            }
        }
    }

    /**
     * Get the {@link Logger} strategy for logging a message with the provided log level.
     * @param logger the logger to be used
     * @param logLevel the level for logging
     * @return the {@link BiConsumer} to be invoked as strategy
     */
    @Nullable
    private Closure<Void> loggerImplFor(@Nonnull final Logger logger, @Nonnull final LogLevel logLevel) {
        switch (logLevel) {
            case LogLevel.DEBUG:
                return { String format, Object[] args -> logger.debug(format, args) }
            case LogLevel.INFO:
                return { String format, Object[] args -> logger.info(format, args) }
            case LogLevel.WARN:
                return { String format, Object[] args -> logger.warn(format, args) }
            case LogLevel.ERROR:
                return { String format, Object[] args -> logger.error(format, args) }
        }
        null
    }

    /**
     * Adds an entry for the provided project.
     * @param project the project
     * @param entry the entry to be added
     */
    private void addEntry(@Nonnull final Project project, @Nonnull final Entry entry) {
        internalMap[project] = internalMap[project] ?: [] as List<Entry>
        internalMap[project] << entry
    }

    @TupleConstructor
    private static class Entry {

        @Nonnull
        private final LogLevel level
        @Nonnull
        private final String format
        @Nonnull
        private final Object[] args

    }

}
