// Trampoline is a technique from functional programming that prevents stack overflow
// in deeply recursive functions.  Instead of calling itself directly (which adds a
// new frame to the call stack on every step), a trampolined function returns a
// "thunk" — a zero-argument closure that represents the next recursive step.
// A driver loop then repeatedly invokes the thunk until a final value is produced.
// Because each recursive call is replaced by a return-value cycle, the call stack
// never grows beyond a constant depth, even for millions of iterations.
def factorial
factorial = { int n, def accu = 1G ->
    if (n < 2) return accu
    factorial.trampoline(n - 1, n * accu)
}
factorial = factorial.trampoline()

assert factorial(1)    == 1
assert factorial(3)    == 1 * 2 * 3
assert factorial(1000) // == 402387260.. plus another 2560 digits
println factorial(1000)
println 'Done'