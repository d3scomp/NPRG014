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

Closure<Result> increment = {Integer v -> v + 1}
//TASK Lift the increment function
Closure<Result> liftedIncrement = ...

//TASK Implement the divideHundredWith so that it returns Some when v!=0 and None otherwise
Closure<Result> divideHundredWith = ...

println ("1. " + (unit(0) >> liftedIncrement >> liftedIncrement >> divideHundredWith))
println ("2. " + (unit(0) >> liftedIncrement >> divideHundredWith >> liftedIncrement))
println ("3. " + (unit(0) >> divideHundredWith >> liftedIncrement >> liftedIncrement))