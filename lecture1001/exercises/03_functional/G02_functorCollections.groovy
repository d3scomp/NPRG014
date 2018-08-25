//doubler: Integer -> Integer
Closure doubler = {2*it}
println ([1, 2, 3, 4, 5].collect(doubler))

//TASK Complete the definition of "map", so that it mimics the behavior of "Collection.collect"
Closure map = {Collection c, Closure f -> ...}
println (map([1, 2, 3, 4, 5], doubler))

//square: Integer -> Integer
Closure square = {it*it}

//TASK complete the definition so that squareWholeList: Collection<Integer> -> Collection<Integer>
//def squareWholeList = ...
//println(squareWholeList([1, 2, 3, 4, 5]))