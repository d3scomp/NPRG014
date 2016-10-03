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

Closure<Result> id = {Integer v -> v}
Closure<Result> liftedId = lift(id)
println("Lifted id: " + liftedId(1))

Closure<Result> increment = {Integer v -> 
    return new Result(value: v+1, description: "Incremented")
}

Closure<Result> doubler = {Integer v -> 
    return new Result(value: 2*v, description: "Doubled")
}

Closure<Result> square = {Integer v -> 
    return new Result(value: v*v, description: "Squared")
}

Closure<Result> triple = {Integer v -> 
    return new Result(value: 3*v, description: "Tripled")
}

def initial = new Result(value: 0, description: '')
def dbound = bind.rcurry(doubler)
def sbound = bind.rcurry(square)
def tbound = bind.rcurry(triple)
def ibound = bind.rcurry(increment)
println ("1. " + (tbound(sbound(dbound(ibound(initial))))))
println ("2. " + (initial >> increment >> doubler >> square >> triple))
println ("3. " + (unit(0) >> increment >> doubler >> square >> triple))

//Bound functions form a monoid
def unitBound = bind.rcurry(unit)
def allBound = [ibound, dbound, sbound, tbound, ibound, ibound, tbound].inject(unitBound){acc, v -> acc >> v}
println ("4. " + allBound(initial))