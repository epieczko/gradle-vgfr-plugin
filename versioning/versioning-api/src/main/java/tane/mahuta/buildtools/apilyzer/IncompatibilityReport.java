package tane.mahuta.buildtools.apilyzer;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Interface for a report
 *
 * @author christian.heike@icloud.com
 *         Created on 19.06.17.
 */
public interface IncompatibilityReport {

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
     * @return true if any incompatible class was listed
     */
    boolean isIncompatible();
}
