//Monad - (IO, unit, bind)

@groovy.transform.Immutable
class IO<A> {
    Closure<A> run
    
    static Closure<IO<A>> unit = {A value ->
        return new IO<A>(run: {return value})
    }

    static Closure<IO> map = {IO param, Closure f ->
        unit(f(param.run.call()))
    }

    //action: A -> IO<B>
    static Closure<Closure<IO>> bind = {IO<A> a, Closure<IO> action ->
        new IO(run: {
            def result = a.run.call()
            action(result).run.call()})
    }
    
    static Closure<IO> lift = {Closure f ->
        return f >> unit
    }
    
    public IO rightShift(Closure<IO> g) {
        bind(this, g)
    }    
}


import static IO.bind
import static IO.unit
import static IO.map
import static IO.lift

Closure<IO<String>> readLine = {ignored -> new IO<String>(run: {System.console().readLine()})}
Closure<IO<Void>> printLine = {String msg -> new IO<Void>(run: {println msg})}
Closure<String> format = {String s -> s.toUpperCase()}

def computation = printLine('What is your name?') >> readLine >> lift(format) >> printLine
println "Computation: " + computation
sleep 2000
computation.run()