package tane.mahuta.buildtools.apilyzer;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Interface for a report of the compatibility of the API of two product versions.
 *
 * @author christian.heike@icloud.com
 *         Created on 19.06.17.
 */
public interface ApiCompatibilityReport {

    /**
     * @return the names of classes which are possibly incompatible
     */
    @Nonnull
    Set<String> getPossibleIncompatibleClasses();

    /**
     * @return the names of classes which are definitely incompatible
     */
    @Nonnull
    Set<String> getDefiniteIncompatibleClasses();

    /**
     * @return {@code true} if the API of all classes are compatible
     */
    boolean isCompatible();
}
