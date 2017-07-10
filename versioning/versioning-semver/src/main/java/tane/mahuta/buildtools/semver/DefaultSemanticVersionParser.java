package tane.mahuta.buildtools.semver;

import lombok.AllArgsConstructor;
import tane.mahuta.buildtools.version.SemanticVersion;
import tane.mahuta.buildtools.version.VersionParser;

import javax.annotation.Nullable;
import java.io.File;

import static lombok.AccessLevel.PROTECTED;

/**
 * Implementation of a {@link VersionParser} for {@link DefaultSemanticVersion}s.
 *
 * @author christian.heike@icloud.com
 *         Created on 14.06.17.
 */
@AllArgsConstructor(access = PROTECTED)
public class DefaultSemanticVersionParser extends AbstractSemanticVersionParser<SemanticVersion> {

    @Override
    protected SemanticVersion doCreate(int major, int minor, @Nullable Integer micro, @Nullable String qualifier, @Nullable File sourceDirectory) {
        return new DefaultSemanticVersion(major, minor, micro, qualifier);
    }

    private static class Holder {
        private static final DefaultSemanticVersionParser INSTANCE = new DefaultSemanticVersionParser();
    }

    public static DefaultSemanticVersionParser getInstance() {
        return Holder.INSTANCE;
    }


}
