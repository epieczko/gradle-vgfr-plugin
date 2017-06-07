package tane.mahuta.gradle.plugin

import org.gradle.api.Project

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 07.06.17.
 */
class TestServiceFactoryImpl implements TestServiceFactory {
    @Override
    String create(@Nonnull Project project) {
        return project.name
    }
}
