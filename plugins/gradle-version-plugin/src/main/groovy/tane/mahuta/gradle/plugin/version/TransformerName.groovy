package tane.mahuta.gradle.plugin.version

import groovy.transform.CompileStatic

/**
 * Names of the {@link tane.mahuta.buildtools.version.VersionTransformer} used by {@link VersionExtension}.
 *
 * @author christian.heike@icloud.com
 * Created on 08.06.17.
 */
@CompileStatic
interface TransformerName {

    final String TO_NEXT_SNAPSHOT = "toNextSnapshot"

    final String TO_RELEASE = "toRelease"

}
