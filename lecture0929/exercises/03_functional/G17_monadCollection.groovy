//Monad - ([], unit, bind)

//Task Implement the missing methods to turn Groovy lists into monads
class Util {    
    static Closure<List> unit = {def v ->
        //...
    }

    static Closure<List> map = {List l, Closure f ->
        l.collect(f)
    }

    static Closure<List> bind = {List a, Closure<List> f ->
      //...
    }

    static Closure<Result> lift = {Closure f ->
        return f >> unit
    }
}

class StringUtil {
    public static List rightShift(List source, Closure<List> g) {
        bind(source, g)
    }

}

import static Util.bind
import static Util.unit
import static Util.map
import static Util.lift

Closure<List> splitChars = {String s -> 
    return s.collect {it}
}

Closure<List> toAscii = {String s -> 
    return s.collect {(int)it}
}

Closure<List> clone = {def v -> 
    return [v, v]
}

Closure<List> tap = {String s -> println s;s}

use(StringUtil) {
    //TASK Use tap to investigate the values at different places of the pipeline
    List values = unit("Dave") >> splitChars >> clone >> toAscii
    println ("Values: " + values)
}