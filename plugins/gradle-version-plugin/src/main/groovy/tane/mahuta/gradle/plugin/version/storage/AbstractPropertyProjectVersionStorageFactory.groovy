package tane.mahuta.gradle.plugin.version.storage

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.Project
import tane.mahuta.buildtools.version.VersionStorage
import tane.mahuta.gradle.plugin.version.ProjectVersionStorageFactory

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
@CompileStatic
@Slf4j
abstract class AbstractPropertyProjectVersionStorageFactory implements ProjectVersionStorageFactory {

    /**
     * Get the file representation of the target file in the provided directory.
     * @param directory the directory
     * @return the file representing the version storage in the directory
     */
    @Nullable
    protected abstract File propertyVersionFileIn(@Nonnull final File directory)

    /**
     * @return the name of the property in the file
     */
    @Nonnull
    protected abstract String propertyName()

    @Override
    VersionStorage create(final Project project) {
        final f = findPropertyFile(project.projectDir)
        final PropertyVersionStorage storage = f != null ? new PropertyVersionStorage(f, propertyName()) : null

        storage?.load() != null ? storage : null
    }

    @Nullable
    private File findPropertyFile(@Nonnull final File directory) {
        def curDir = directory
        while (curDir != null) {
            final result = propertyVersionFileIn(curDir)
            if (result?.isFile()) {
                log.debug("Found version property file: {}", result)
                return result
            }
            curDir = curDir.getParentFile()
        }
        log.debug("Could not find property file recursively upwards in: {}", directory)
        return null
    }
}
