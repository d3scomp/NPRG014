//Monad - (Maybe, unit, bind)

@groovy.transform.Immutable
class Request {
    String name
    String email
    Integer age
}

@groovy.transform.ToString
class Maybe {
    Request value
    
    static Closure<Maybe> unit = {Request v ->
        return new Some(value: v)
    }

    static Closure<Maybe> map = {Maybe m, Closure f ->
        if (m instanceof None) return new None()
        unit(f(m.value))
    }
    
    static Closure<Maybe> bind = {Maybe a, Closure<Maybe> f ->
        if (a instanceof None) return new None()
        else return f(a.value)
    }

    static Closure<Maybe> lift = {Closure f ->
        return f >> unit
    }
    
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

def validRequest = new Request('Joe', 'joe@mycompany.com', 23)
def invalidEmail = new Request('Joe', 'joe # mycompany.com', 23)
def invalidAge = new Request('Joe', 'joe@mycompany.com', 17)

Closure<Maybe> checkName = {Request r -> r.name!=null && r.name.size()>0 ? new Some(value: r) : new None()}
Closure<Maybe> checkEmail = {Request r -> 
    r.email!=null && r.email.contains('@') ? new Some(value: new Request(r.name, r.email.toUpperCase(), r.age)) : new None()
}
Closure<Maybe> checkAge = {Request r -> r.age >= 18 ? new Some(value: r) : new None()}

println ("Result for valid request: " + (unit(validRequest) >> checkName >> checkEmail >> checkAge))
println ("Result for invalid email: " + (unit(invalidEmail) >> checkName >> checkEmail >> checkAge))
println ("Result for invalid age: " + (unit(invalidAge) >> checkName >> checkEmail >> checkAge))

//TASK Make the None class hold a textual description of the reason, why the request failed to be validated