package tane.mahuta.gradle.plugins.version.persistence.impl

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author christian.heike@icloud.com
 * Created on 01.06.17.
 */
class PropertyVersionStorageTest extends Specification {

    @Rule
    final TemporaryFolder folder = new TemporaryFolder()

    static final INITIAL_VERSION = "1.2.3"
    static final NEW_VERSION = "1.2.4-SNAPSHOT"


    private PropertyVersionStorage storage

    def setup() {
        final file = new File(folder.getRoot(), "temp.properties")
        file.createNewFile()
        storage = new PropertyVersionStorage(file, "version")
        storage.store(INITIAL_VERSION)
    }

    def "throws exception, if no file provided"() {
        when:
        new PropertyVersionStorage(folder.getRoot(), "version")
        then:
        thrown(IllegalArgumentException)
    }

    def "stores a new version"() {
        when: 'storing a version'
        storage.store(NEW_VERSION)
        then: 'the loaded version is equal'
        storage.load() == NEW_VERSION
    }

    def "does not write the same version"() {
        setup:
        final ts = storage.@file.lastModified()

        when: 'storing the same version again'
        storage.store(INITIAL_VERSION)
        then: 'the loaded version is still equal'
        storage.load() == INITIAL_VERSION
        and: 'the file has not changed'
        ts == storage.@file.lastModified()
    }

    def "loading the version from the file"() {
        expect: 'the loaded version equals it'
        storage.load() == INITIAL_VERSION
    }

}
