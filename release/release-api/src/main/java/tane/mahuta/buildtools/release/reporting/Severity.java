package tane.mahuta.buildtools.release.reporting;

/**
 * Severity for a {@link ReleaseProblem}.
 *
 * @author christian.heike@icloud.com
 *         Created on 20.06.17.
 */
public enum Severity {

    /**
     * A warning, which means the release can continue.
     */
    WARNING,
    /**
     * A reporting, which means the release cannot continue.
     */
    PROBLEM

}
