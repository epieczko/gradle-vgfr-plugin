package tane.mahuta.build.version;

/**
 * Interface for a version.
 *
 * @author christian.heike@icloud.com
 *         Created on 06.06.17.
 */
public interface Version {

    /**
     * @return a storable representation of the version
     */
    Object toStorable();

}
