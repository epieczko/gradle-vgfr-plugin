package tane.mahuta.gradle.plugin.version.transform

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
@Subject(ChangeLevel)
class ChangeLevelTest extends Specification {

    @Unroll
    def "description of #value is not null"() {
        expect:
        value.description != null

        where:
        value << ChangeLevel.values()
    }

}
