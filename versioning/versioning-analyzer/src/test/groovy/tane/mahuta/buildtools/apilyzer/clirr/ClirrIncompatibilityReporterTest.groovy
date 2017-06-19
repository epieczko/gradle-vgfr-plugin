package tane.mahuta.buildtools.apilyzer.clirr

import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import tane.mahuta.buildtools.apilyzer.Scope

/**
 * @author christian.heike@icloud.com
 * Created on 19.06.17.
 */
class ClirrIncompatibilityReporterTest extends Specification {

    @ClassRule
    @Shared
    @Delegate
    static final TemporaryFolder folderRule = new TemporaryFolder()

    def setupSpec() {
        folderRule.create()
        ['1.1', '2.0', '2.4', '2.5'].each { version ->
            new URL("http://central.maven.org/maven2/commons-io/commons-io/${version}/commons-io-${version}.jar").withInputStream { is ->
                new File(root, "commons-io-${version}.jar").withOutputStream { os -> os << is }
            }
        }
    }

    def cleanupSpec() {
        folderRule.delete()
    }

    @Unroll
    def 'analysis of #curVer vs #baseVer finds #definiteSize/#possibleSize classes (d/p)'() {
        setup:
        final source = new File(root, "commons-io-${curVer}.jar")
        final target = new File(root, "commons-io-${baseVer}.jar")
        final reporter = new ClirrIncompatibilityReporter().withScope(Scope.PUBLIC).withCurrent(source).withBaseline(target).withLogger(LoggerFactory.getLogger(getClass()))
        final incompatible = (definiteSize + possibleSize) > 0
        when:
        final report = reporter.buildReport()

        then:
        report.definiteIncompatibleClasses.size() == definiteSize
        and:
        report.possibleIncompatibleClasses.size() == possibleSize
        and:
        report.isIncompatible() == incompatible

        where:
        curVer | baseVer | definiteSize | possibleSize
        '2.5'  | '2.4'   | 0            | 0
        '2.5'  | '1.1'   | 0            | 0
        '2.5'  | '2.0'   | 0            | 0
        '2.0'  | '2.4'   | 13           | 0
        '1.1'  | '2.4'   | 85           | 0
    }

}
