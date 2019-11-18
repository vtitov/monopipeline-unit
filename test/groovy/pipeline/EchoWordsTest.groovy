package pipeline

import testSupport.PipelineSpockTestBase
import utils.SharedLibraryHelpers

class EchoWordsTest extends PipelineSpockTestBase implements SharedLibraryHelpers {
    def "echo-words"() {
        given:
        [FOO: foo, BAR: bar].each {k,v->binding.setVariable(k,v)}

        when:
        runScript('jobs/sandbox/echo-words.jenkins')

        then:
        printCallStack()
        assertJobStatusSuccess()

        then:
        testNonRegression("Jenkinsfile_should_complete_with_success")

        where:
        foo = 'foo'; bar = 'bar'
    }
}
