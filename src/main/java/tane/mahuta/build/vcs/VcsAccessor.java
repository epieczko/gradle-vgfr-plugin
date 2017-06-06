package tane.mahuta.build.vcs;

/**
 * Interface for <b>V</b>ersion <b>C</b>ontrol <b>S</b>ystem accessor.
 *
 * @author christian.heike@icloud.com
 *         Created on 05.06.17.
 */
public interface VcsAccessor {

    /**
     * @return the branch name
     */
    String getBranch();

    /**
     * @return the revision id
     */
    String getRevisionId();

}
