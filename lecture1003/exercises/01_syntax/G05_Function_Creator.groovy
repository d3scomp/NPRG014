Closure multiply = {a, b -> a * b}

//TASK Implement the 'creator' function leveraging the "multiply" function above
//so that it creates correct multiplication functions

Closure creator = {n ->

}

Closure quadruple = creator 4
assert 40 == quadruple(10)
assert 30 == creator(3)(10)

println 'ok'