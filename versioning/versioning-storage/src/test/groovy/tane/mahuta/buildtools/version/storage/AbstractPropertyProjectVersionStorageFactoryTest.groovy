package tane.mahuta.buildtools.version.storage

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.Nonnull
/**
 * @author christian.heike@icloud.com
 * Created on 02.06.17.
 */
@Subject(AbstractPropertyProjectVersionStorageFactory)
class AbstractPropertyProjectVersionStorageFactoryTest extends Specification {

    @Rule
    final TemporaryFolder folderRule = new TemporaryFolder()

    private
    final AbstractPropertyProjectVersionStorageFactory factory = new AbstractPropertyProjectVersionStorageFactory() {

        @Override
        protected File propertyVersionFileIn(@Nonnull final File directory) {
            return new File(directory, "test.txt")
        }

        @Override
        protected String propertyName() {
            return "version"
        }
    }

    def setup() {
    }

    def 'create with no file present, does return null'() {
        expect:
        factory.create(folderRule.root) == null
    }

   def 'create with file returns storage'() {
       setup:
       final f = new File(folderRule.root, "test.txt")
       f.createNewFile()

       when:
       final storage = factory.create(new File(folderRule.root, "a/b"))
       then:
       storage != null
       and:
       storage.@propertyName == 'version'
       and:
       storage.@file == f
   }

}
