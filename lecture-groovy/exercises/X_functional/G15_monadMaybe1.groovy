//Monad - (Maybe, unit, bind)

@groovy.transform.ToString
class Maybe {
    Integer value
    
    //Turns an Integer value into a Maybe
    static Closure<Maybe> unit = {Integer v ->
        return new Some(value: v)
    }

    //Maps the Integer value using the provided function
    //f: Integer -> Integer
    static Closure<Maybe> map = {Maybe m, Closure f ->
        if (m instanceof None) return new None()
        unit(f(m.value))
    }
    
    //f: Integer -> Maybe
    static Closure<Maybe> bind = {Maybe a, Closure<Maybe> f ->
        if (a instanceof None) return new None()
        else return f(a.value)
    }

    //Allows plain Integer -> Integer functions to join the monad
    //f: Integer -> Integer
    //returns: Integer -> Maybe
    static Closure<Maybe> lift = {Closure f ->
        return f >> unit
    }
    
    //Binds the function and applies it to this Maybe
    //f: Integer -> Maybe
    //returns: Maybe   
    public Maybe rightShift(Closure<Maybe> g) {
        bind(this, g)
    }
}

@groovy.transform.ToString(includeSuper = true)
class Some extends Maybe {}

@groovy.transform.ToString
class None extends Maybe {}

import static Maybe.bind
import static Maybe.unit
import static Maybe.map
import static Maybe.lift

//Create monadic data elements
println ("Unit: " + unit(0))

//Map a plain function over a Maybe
println ("Map: " + (map(unit(5)) {it * 3}))

//Create monadic functions out of plain functions
Closure<Maybe> increment = {Integer v -> v + 1}
Closure<Maybe> liftedIncrement = lift(increment)
println("Lifted: " + liftedIncrement(0))

//Bind monadic functions to monadic data parameters
println ("Bound: " + bind(unit(0), liftedIncrement))

//Apply monadic function to monadic data
println ("Applied: " + (unit(0) >> liftedIncrement))