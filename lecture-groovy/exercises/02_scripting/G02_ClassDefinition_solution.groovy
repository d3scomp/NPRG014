//These two simulate the program input parameters
final properties= ['name': String, 'age': Integer]
final className = "Person"

def binding = new Binding()

GroovyShell shell = new GroovyShell(binding)

String code = """
class Person {
    String name
    int age
}

return Person.class
"""

def cls = shell.evaluate(code)

def object = cls.newInstance()
object.name = "Joe"
object['age'] = 20
println 'Call the object: ' + object.name + " age " + object['age']

def object2 = cls.newInstance()
println 'Same class for both objects? =====> ' + (object.class == object2.class) + ' <====='
