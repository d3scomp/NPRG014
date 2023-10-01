def joe = [name : 'Joe', age : 83]
def jeff = [name : 'Jeff', age : 38]
def jess = [name : 'Jess', age : 33]

def process(person, code) {
//    code.delegate = person    
//    code.resolveStrategy = Closure.DELEGATE_FIRST
    code.call()
//    person.with(code)
}

name = "Noname"
process(joe, {println name})
//process(jeff, {println age})


/*
class Person {
    final name = "Whatever"
    final greet = {println name}
}
process(joe, new Person().greet)
*/


//TASK Experiment with owner, delegate as well as with different resolution strategies