// Memoization is an optimization technique commonly used in functional programming.
// It caches the result of a pure function call for a given set of arguments,
// so that subsequent calls with the same arguments can return the cached value
// instead of recomputing it.  This turns expensive repeated calculations — such as
// the exponential number of recursive calls in a naïve Fibonacci implementation —
// into fast constant-time lookups, at the cost of additional memory for the cache.
def fib = null
fib = {
    it <= 1 ? it : (fib(it - 2) + fib(it - 1))
}.memoize()

long b = System.currentTimeMillis()
fib(90G)
long a = System.currentTimeMillis()
println((a - b) / 1000 + ' seconds') // about 0.002 seconds on my machine