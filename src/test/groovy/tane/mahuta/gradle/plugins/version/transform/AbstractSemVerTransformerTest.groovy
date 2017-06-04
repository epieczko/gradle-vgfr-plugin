package tane.mahuta.gradle.plugins.version.transform

import spock.lang.Specification
import tane.mahuta.build.version.SemanticVersion
import tane.mahuta.build.version.VersionTransformer

import javax.annotation.Nonnull

/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
class AbstractSemVerTransformerTest extends Specification {

    private final VersionTransformer mock = Mock(VersionTransformer)
    private final AbstractSemVerTransformer transformer = new AbstractSemVerTransformer() {
        @Override
        SemanticVersion transformSemanticVersion(@Nonnull SemanticVersion semanticVersion) {
            mock.transform(semanticVersion)
        }
    }

    def 'parses version strings'() {
        setup:
        final source = "1.2.3"
        final transformed = SemanticVersion.parse("1.2.4")

        when:
        final actual = transformer.transform(source)
        then:
        1 * mock.transform(SemanticVersion.parse("1.2.3")) >> transformed
        and:
        actual.is(transformed)
    }

    def 'uses semantic versions without conversion'() {
        setup:
        final source = SemanticVersion.parse("1.2.3")
        final transformed = SemanticVersion.parse("1.2.4")

        when:
        final actual = transformer.transform(source)
        then:
        1 * mock.transform({ it.is(source) }) >> transformed
        and:
        actual.is(transformed)
    }
}
