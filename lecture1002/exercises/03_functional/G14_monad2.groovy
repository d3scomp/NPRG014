//Monad - (Result, unit, bind)

@groovy.transform.ToString
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
    
    //Binds the function and applies it to this Result
    //f: Integer -> Result<Integer, String>
    //returns: Result<Integer, String>    
    public Result rightShift(Closure<Result> g) {
        bind(this, g)
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

//Apply a monadic function to monadic data
println ("Applied: " + (unit(0) >> increment >> triple))