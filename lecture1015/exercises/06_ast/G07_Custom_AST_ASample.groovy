//TASK compile the required transformation classes by invoking "groovyc Custom_AST.groovy" from the command line and check to see the generated .class files
//TASK explore in the AST Viewer the modifications imposed by the transformation

class Calculator {
    @Requires("divisor != 0")
    public int divide10By(divisor) {
        10 / divisor
    }

    @Requires("a > b")
    public int subtract(a, b) {
        a - b
    }
}

final calculator = new Calculator()


println calculator.divide10By(5)
println calculator.subtract(10, 5)

//TASK uncomment the following lines and see the reported problems
//println calculator.divide10By(0)
//println calculator.subtract(5, 10)