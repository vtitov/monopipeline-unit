package script

import org.junit.Test

class SomeMathTest extends GroovyShellTestCase {
    @Test
    void testIniFile() {
        log.fine("some math test")
        Binding binding = new Binding()
        binding.x = 3; binding.y = 4

        GroovyShell shell = new GroovyShell(binding)
        shell.evaluate(new File('test/jenkins/scripts/someMath.groovy'))
        assertEquals 7, binding.z
    }
}
