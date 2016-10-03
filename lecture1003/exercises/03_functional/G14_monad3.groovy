//Monad - (Result, unit, bind)

@groovy.transform.ToString
class Result {
    Integer value
    String description
    
    //Turns an Integer value into a Result(Integer, String)
    static Closure<Result> unit = {Integer v ->
        return new Result(value: v, description: "")
    }

    //Maps the Integer value using the provided plain function
    //f: Integer -> Integer
    static Closure<Result> map = {Result r, Closure<Integer> f ->
        unit(f(r.value))
    }

    //f: Integer -> Result<Integer, String>
    static Closure<Result> bind = {Result a, Closure<Result> f ->
        def calculated = f(a.value)
        return new Result(value: calculated.value, description: a.description + 
        (a.description?.size()>0 && calculated.description?.size()>0 ? '. ' : '') + 
        calculated.description)
    }

    //Allows plain Integer -> Integer function to join the monad
    //f: Integer -> Integer
    //returns: Integer -> Result<Integer, String>
    static Closure<Result> lift = {Closure<Integer> f ->
        return f >> unit
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
import static Result.map
import static Result.lift

//Create monadic data elements
println ("Unit: " + unit(1))

Closure<Integer> plainIncrement = {Integer v -> v + 1}

//Map a plain function over a monadic value
println ("Map: " + map(unit(10), plainIncrement))

//Create monadic functions out of plain functions
Closure<Result> liftedIncrement = lift(plainIncrement)
println("Lifted: " + liftedIncrement(100))

//Bind monadic functions to monadic data parameters
println ("Bound" + bind(unit(200),liftedIncrement))

//Apply a monadic function to monadic data
println ("Applied: " + (unit(300) >> liftedIncrement))