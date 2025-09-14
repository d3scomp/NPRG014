import org.codehaus.groovy.control.CompilerConfiguration

//TASK use scripting to supply custom commands provided at run-time
String myCode = '''
create house
move furniture
'''

def config = new CompilerConfiguration()
config.scriptBaseClass = 'Robot'
GroovyShell shell = new GroovyShell(this.class.classLoader, config)

shell.evaluate(myCode)

abstract class Robot extends Script{
    def propertyMissing(String name) {
        "*${name.toUpperCase()}*"
    }

    def methodMissing(String name, args) {
        println "${name[0].toUpperCase() + name[1..-2]}ing ${args.join(', ')} as requested"
    }
}