import org.codehaus.groovy.control.CompilerConfiguration

abstract class MyBaseClass extends Script {
    String name
    void greet() {
        println "Hello $name"
    }
    void say(String word) {
        println "I am saying $word"
    }
}

def config = new CompilerConfiguration()
config.scriptBaseClass = 'MyBaseClass'
GroovyShell shell = new GroovyShell(this.class.classLoader, config)

final code = '''
name = 'Ingrid'
greet()
say 'Hi'
say 'Bye'
'''

println 'Result: ' + shell.evaluate(code)
