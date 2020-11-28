def fib = null
fib = {
    it <= 1 ? it : (fib(it - 2) + fib(it - 1))
}.memoize()

//TASK Use the memoizeAtMost() method instead and find the minimum number of remembered elements for the algorithm to run in linear space and time

long b = System.currentTimeMillis()
fib(90G)
long a = System.currentTimeMillis()
println((a - b) / 1000 + ' seconds') // about 0.002 seconds on my machine