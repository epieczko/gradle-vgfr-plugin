package tane.mahuta.buildtools.version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author christian.heike@icloud.com
 *         Created on 19.06.17.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SemanticVersionTransformers {

    public static final VersionTransformer<SemanticVersion, String> LAST_RELEASE_VERSION_SELECTOR = source -> {
        final StringBuilder sb = new StringBuilder();
        // TODO implement
        sb.append("X");
        return sb.toString();
    };


}
