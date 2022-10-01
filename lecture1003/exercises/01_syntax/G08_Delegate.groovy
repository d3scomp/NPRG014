def joe = [name : 'Joe', age : 83]
def jeff = [name : 'Jeff', age : 38]
def jess = [name : 'Jess', age : 33]

def process(person, code) {
//    code.delegate = person    
//    code.resolveStrategy = Closure.DELEGATE_FIRST
    code.call()
//    person.with(code)
}

class FamilyManager {
    String name = "Nobody"
    int age = 0
    
    def greet = {println "Greeting $name"}
    
    def celebrate = {
            println "Celebrating birthday of " + name
            age+=1
        }
}

def fm = new FamilyManager()
process(joe, fm.greet)
process(jeff, fm.greet)
process(jess, fm.greet)

process(jess, fm.celebrate)
assert 34 == jess.age

//fm.greet.call()
//fm.celebrate()

//TASK Experiment with owner, delegate as well as with different resolution strategies