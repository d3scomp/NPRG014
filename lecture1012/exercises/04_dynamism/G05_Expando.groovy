interface Calculator {
    def add(a, b)

    def subtract(a, b)

    def multiply(a, b)

    def divide(a, b)

    def increment(a)
}

final expandoCalculator = new Expando()
expandoCalculator.add = {a, b -> a + b}
expandoCalculator.multiply = {a, b -> a * b}
expandoCalculator.increment = {add(it, 1)}

final Calculator calculator = expandoCalculator as Calculator
assert 10 == calculator.add(3, 7)
assert 6 == calculator.multiply(2, 3)
assert 6 == calculator.increment(5)

println 'done'