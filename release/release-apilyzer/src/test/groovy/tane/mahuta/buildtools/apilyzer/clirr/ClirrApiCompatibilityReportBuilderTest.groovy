package tane.mahuta.buildtools.apilyzer.clirr

import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import org.slf4j.LoggerFactory
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import tane.mahuta.buildtools.apilyzer.Scope
import tane.mahuta.gradle.plugin.OnlineCheck

/**
 * @author christian.heike@icloud.com
 * Created on 19.06.17.
 */
class ClirrApiCompatibilityReportBuilderTest extends Specification {

    @ClassRule
    @Shared
    @Delegate
    static final TemporaryFolder folderRule = new TemporaryFolder()

    def setupSpec() {
        if (OnlineCheck.online) {
            folderRule.create()
            ['1.1', '2.4', '2.5'].each { version ->
                new URL("http://central.maven.org/maven2/commons-io/commons-io/${version}/commons-io-${version}.jar").withInputStream { is ->
                    new File(root, "commons-io-${version}.jar").withOutputStream { os -> os << is }
                }
            }
        }
    }

    def cleanupSpec() {
        folderRule.delete()
    }

    @Unroll
    @Requires({ OnlineCheck.online })
    def 'analysis of #curVer vs #baseVer scope #scope packages #packages classes #classes finds #definiteSize/#possibleSize classes (d/p)'() {
        setup:
        final source = new File(root, "commons-io-${curVer}.jar")
        final target = new File(root, "commons-io-${baseVer}.jar")
        final reporter = new ClirrApiCompatibilityReportBuilder()
                .withScope(scope)
                .withCurrent(source)
                .withCurrentClasspath([])
                .withBaseline(target)
                .withBaselineClasspath([])
                .withLogger(LoggerFactory.getLogger(getClass()))
                .withPackages(packages.collect { "org.apache.commons.io.${it}" as String })
                .withClasses(classes.collect { "org.apache.commons.io.${it}" as String })
        final compatible = (definiteSize + possibleSize) == 0
        when:
        final report = reporter.buildReport()

        then:
        report.definiteIncompatibleClasses.size() == definiteSize
        and:
        report.possibleIncompatibleClasses.size() == possibleSize
        and:
        report.compatible == compatible

        where:
        curVer | baseVer | scope           | packages       | classes                         | definiteSize | possibleSize
        '2.5'  | '2.4'   | Scope.PUBLIC    | []             | []                              | 0            | 0
        '2.5'  | '1.1'   | Scope.PROTECTED | []             | []                              | 0            | 0
        '2.5'  | '1.1'   | Scope.PACKAGE   | []             | []                              | 0            | 0
        '2.5'  | '1.1'   | Scope.PRIVATE   | []             | []                              | 14           | 0
        '1.1'  | '2.5'   | Scope.PRIVATE   | ['filefilter'] | []                              | 22           | 0
        '1.1'  | '2.5'   | Scope.PRIVATE   | []             | ['filefilter.HiddenFileFilter'] | 1            | 0
    }

}
