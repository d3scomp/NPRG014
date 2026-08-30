//Monad - (Result, unit, bind)

@groovy.transform.ToString
@groovy.transform.Immutable
class Result {
    Integer value
    String description
    
    //Turns an Integer value into a Result(Integer, String)
    static Closure<Result> unit = {Integer v ->
        return new Result(value: v, description: "")
    }

    //f: Integer -> Result<Integer, String>
    static Closure<Result> bind = {Result a, Closure<Result> f ->
        def calculated = f(a.value)
        return new Result(value: calculated.value, description: a.description + 
        (a.description?.size()>0 && calculated.description?.size()>0 ? '. ' : '') + 
        calculated.description)
    }
}

import static Result.bind
import static Result.unit

//Create monadic data elements
println ("Unit: " + unit(0))

def increment = {Integer value -> new Result(value: value + 1, description: "Incremented")}
def triple = {Integer value -> new Result(value: value * 3, description: "Tripled")}

//Bind monadic functions to monadic data parameters
Result data = unit(0)
Result incremented = bind(data, increment)
println incremented

Result tripled = bind(incremented, triple)
println tripled

//Monad laws
Result m = unit(20)
def f = increment
def g = triple

//1.
assert bind(unit(10), f) == f(10)

//2.
assert bind(m, unit) == m

//3.
def v1 = bind(bind(m, f), g)
def v2 = bind(m, {x -> bind(f(x), g)})
assert v1 == v2