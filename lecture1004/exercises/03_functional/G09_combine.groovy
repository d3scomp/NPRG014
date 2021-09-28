class Person {
    String name
    int age
}

//The '>>' operator is a 'combine' operation, which combines two functions with compatible types
//TASK Is this a monoid?

//Person -> Integer
Closure<Integer> retrieveAge = {Person p -> p.age}

//Integer -> String
Closure<String> createReport = {Integer value -> "The age is: $value"}

//Person -> String
Closure<String> createPersonReport = retrieveAge >> createReport

def report = createPersonReport(new Person(name: "Joe", age: 32))
println report

