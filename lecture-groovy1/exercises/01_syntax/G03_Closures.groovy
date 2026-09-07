Closure multiply = {a, b -> a * b}

assert 6 == multiply(2, 3)

assert "MFF" == "M" + multiply("F", 2)

//TASK Make increment to use the implicit parameter
Closure increment = {a ->
    if (a < 100) return a + 1
    else return a
}

assert 11 == increment(10)
assert 100 == increment(100)

println 'ok'