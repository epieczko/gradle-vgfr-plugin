package tane.mahuta.gradle.plugin.release

import org.gradle.api.Project
import tane.mahuta.buildtools.version.ChangeLevel

import javax.annotation.Nonnull
/**
 * Interface for an analyzer which determines the {@link ChangeLevel} for a {@link Project}.
 *
 * @author christian.heike@icloud.com
 * Created on 12.06.17.
 */
interface ChangeLevelAnalyzer {

    /**
     * Determine the {@link ChangeLevel} for the provided project.
     * @param project the project
     * @return the change level for the project
     */
    @Nonnull
    ChangeLevel determineChangeLevel(@Nonnull final Project project)

}