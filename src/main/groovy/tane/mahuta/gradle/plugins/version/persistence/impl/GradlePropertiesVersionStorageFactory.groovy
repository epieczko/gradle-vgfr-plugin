package tane.mahuta.gradle.plugins.version.persistence.impl

import groovy.transform.CompileStatic

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * @author christian.heike@icloud.com
 * Created on 28.05.17.
 */
@CompileStatic
class GradlePropertiesVersionStorageFactory extends AbstractPropertyVersionStorageFactory {

    @Override
    @Nullable
    protected File propertyVersionFileIn(@Nonnull final File directory) {
        new File(directory, "gradle.properties")
    }

    @Override
    protected String propertyName() {
        "version"
    }

}
