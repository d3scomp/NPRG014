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
println code


def object = shell.evaluate(code)
object.name = "Joe"
object['age'] = 20
println 'Call the object: ' + object.name + " age " + object['age']

//TASK modify so that a class definition is returned from the "code" and the new instance is created in the main script