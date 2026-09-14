import groovy.transform.ToString
import groovy.transform.TupleConstructor

@TupleConstructor @ToString class Person {
    String firstName
    String secondName
    int age
}

return new Person('Joe', 'Smith', 30)

//TASK Explore the class in Ast Inspector and explain the effect of the annotation
