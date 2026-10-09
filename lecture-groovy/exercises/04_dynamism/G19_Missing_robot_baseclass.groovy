import org.codehaus.groovy.control.CompilerConfiguration

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