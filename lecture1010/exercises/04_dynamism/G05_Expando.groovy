interface Calculator {
    def add(a, b)

    def subtract(a, b)

    def multiply(a, b)

    def divide(a, b)

    def increment(a)
}

final myCalculator = new Expando()
myCalculator.add = {a, b -> a + b}
myCalculator.multiply = {a, b -> a * b}
myCalculator.increment = {add(it, 1)}

assert 10 == myCalculator.add(3, 7)
assert 6 == myCalculator.multiply(2, 3)
assert 6 == myCalculator.increment(5)

println 'done'