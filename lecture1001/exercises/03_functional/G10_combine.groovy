Closure<Integer> doubler = {2 * it}
Closure<Integer> square = {2 * it}
Closure<Boolean> isDivisibleBy16 = {it % 16 == 0}

def combined = doubler >> square >> isDivisibleBy16

println([1, 2, 3, 4, 5, 6, 7, 8, 9].collect {combined(it)})
