//These two simulate the program input parameters
final properties= ['name': String, 'age': Integer]
final className = "Person"

def binding = new Binding()

GroovyShell shell = new GroovyShell(binding)

String code = "class $className {\n"
properties.each {entry ->
    code += "\t${entry.value.name} ${entry.key}\n"
}
code += "}\n\n"
code += "return new $className()"
println "===== String to evaluate =====\n"
println code
println "===== end ====="

def object = shell.evaluate(code)

object.name = "Joe"
object['age'] = 20
println 'Call the object: ' + object.name + " age " + object['age']

def object2 = shell.evaluate(code)
println 'Same class for both objects? =====> ' + (object.class == object2.class) + ' <====='
//TASK Explain the result on the line above
//TASK modify so that a class definition is returned from the "code" and the new instance is created in the main script
