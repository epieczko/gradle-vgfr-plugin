package tane.mahuta.gradle.plugin

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.extension.Parameters
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import groovy.transform.CompileStatic
/**
 * @author christian.heike@icloud.com
 * Created on 06.07.17.
 */
@CompileStatic
class StorageTransformer extends ResponseDefinitionTransformer {

    private Map<String, byte[]> contents = [:]

    @Override
    ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        final String path = normalizePath(request.url)
        if (request.method == RequestMethod.PUT) {
            contents[path] = request.body
        } else if (request.method == RequestMethod.GET) {
            if (hasContent(path)) {
                return ResponseDefinitionBuilder.responseDefinition().withBody(contents[path]).withStatus(200).build()
            }
        } else if (request.method == RequestMethod.HEAD) {
            if (hasContent(path)) {
                return ResponseDefinitionBuilder.responseDefinition().withStatus(200).build()
            }
        }
        responseDefinition
    }

    boolean hasContent(final String path) {
        return contents[normalizePath(path)] != null
    }

    @Override
    String getName() {
        return getClass().getSimpleName()
    }

    private static String normalizePath(final String path) {
        !path?.startsWith('/') ? "/${path}" : path
    }

}
