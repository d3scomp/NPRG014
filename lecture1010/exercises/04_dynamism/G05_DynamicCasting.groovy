class ComputerPerson {
    String name
    int age

    String toString() {
        "Person $name $age"
    }

    def asType(Class clazz) {
        switch (clazz) {
            case Integer: return age
            case Programmer: return new Programmer(nickName: name + ' the code master')
        }
    }

    def asBoolean() {
        return false
    }
}

class Programmer {
    String nickName

    String toString() {
        "Programmer $nickName"
    }

    def asBoolean() {
        return true
    }
}

def a = new ComputerPerson(name: 'Joe', age: 25)
println a
assert !a
println 'Age: ' + (a as Integer)

def b = (a as Programmer)
println b
assert b



println 'ok'