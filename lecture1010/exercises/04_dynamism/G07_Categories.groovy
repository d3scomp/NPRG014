class MyMath {
    static int factorial(Integer value) {
        value > 1 ? value * factorial(value - 1) : 1
    }
}

use(MyMath) {
    println 1.factorial()
    println 10.factorial()
}

//TASK Calculate factorial of 12 outside of the scope of the category
//assert 479001600 == 

println 'done'