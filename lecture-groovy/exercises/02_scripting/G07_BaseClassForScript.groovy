// Demonstrates how to programmatically set a custom base script class (MyBaseClass) 
// using CompilerConfiguration and GroovyShell. This allows the dynamically evaluated 
// script string to directly access the custom properties and methods defined in the base class.

import org.codehaus.groovy.control.CompilerConfiguration

abstract class MyBaseClass extends Script {
    String myName
    void greet() {
        println "Hello $myName"
    }
    void say(String word) {
        println "I am saying $word"
    }
}

def config = new CompilerConfiguration()
config.scriptBaseClass = 'MyBaseClass'
GroovyShell shell = new GroovyShell(this.class.classLoader, config)

final code = '''
myName = 'Ingrid'
greet()
say 'Hi'
say 'Bye'

return myName
'''

println 'Result: ' + shell.evaluate(code)