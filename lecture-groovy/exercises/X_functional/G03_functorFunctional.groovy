class Counter {
    Integer value    
}
Closure<Counter> map = {Counter counter, Closure<Integer> f ->
        new Counter(value: f(counter.value))
}

Closure<Integer> increment = {Integer v -> v + 1}
Closure<Integer> duplicate = {Integer v -> 2*v}

Counter c = new Counter(value: 0)
Counter r1 = map(c, increment)
println r1.value

//Counter<Integer> -> Counter<Integer>
Closure<Counter> duplicateCounter = map.rcurry(duplicate)
Counter r2 = duplicateCounter(r1)
println r2.value