package tane.mahuta.buildtools.version.storage

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * @author christian.heike@icloud.com
 * Created on 01.06.17.
 */
@Subject(PropertyVersionStorage)
class PropertyVersionStorageTest extends Specification {

    @Rule
    final TemporaryFolder folderRule = new TemporaryFolder()

    @Unroll
    def '#template new version in file'() {
        setup:
        final source = getClass().getResourceAsStream("${template}-source.properties").text
        final expected = getClass().getResourceAsStream("${template}-expected.properties").text

        final propFile = new File(folderRule.root, "test.txt")
        propFile.text = source

        when:
        final storage = new PropertyVersionStorage(propFile, "version")
        then:
        storage != null

        when:
        storage.store("New versioN")
        then:
        propFile.text == expected

        where:
        template << [
                'colon', 'equals', 'missing', 'multiline', 'space'
        ]
    }

    def 'throws exception in case no file was provided'() {
        setup:
        final propFile = new File(folderRule.root, "test.txt")
        propFile.mkdir()

        when:
        new PropertyVersionStorage(propFile, "version")
        then:
        thrown(IllegalArgumentException)
    }

}
