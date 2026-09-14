import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.CompilationCustomizer
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.codehaus.groovy.control.customizers.SecureASTCustomizer

CompilerConfiguration config = new CompilerConfiguration()
config.targetDirectory = new File('./compiled')

CompilationCustomizer myImports = new ImportCustomizer()
myImports.addStaticImport('sinus', 'java.lang.Math', 'sin')
myImports.addStaticImport('cosinus', 'java.lang.Math', 'cos')

CompilationCustomizer mySecurity = new SecureASTCustomizer()
mySecurity.receiversClassesBlackList = [System]

config.addCompilationCustomizers(myImports, mySecurity)


def binding = new Binding()
binding['a'] = 10
binding['b'] = 20

GroovyShell shell = new GroovyShell(binding, config)

final code = '''
println 'Calculating ...'
sinus(a) + cosinus(b) + 9

'''

println 'Result: ' + shell.evaluate(code)

//TASK Try to violate the security constraint by calling System.exit() from within the script