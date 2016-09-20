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
    //returns: Result<Integer, String> -> Result<Integer, String>
    static Closure<Closure<Result>> bind = {Closure<Result> f ->
        return {Result r -> 
            def calculated = f(r.value)
            return new Result(value: calculated.value, description: r.description + 
            (r.description?.size()>0 && calculated.description?.size()>0 ? '. ' : '') + 
            calculated.description)
        }
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
Result incremented = bind(increment)(data)
println incremented

Result tripled = bind(triple)(incremented)
println tripled
