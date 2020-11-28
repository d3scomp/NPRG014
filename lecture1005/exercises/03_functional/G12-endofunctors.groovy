//TASK Turn this into a monoid for functions String -> Integer

//All functions are String -> Integer
Closure<Integer> id = //Implement
Closure<Integer> length = {String value -> value==null ? 0 : value.size()}
Closure<Integer> numOfCapital = {String value -> value.findAll {c -> c != c.toLowerCase()}.size()}
Closure<Integer> numOfSpaces = {String value -> value.findAll(' ').size()}

Closure<Closure<Integer>> myCombine = //Implement

def myCalculation = myCombine(length, numOfCapital)
println myCalculation("Hello, how are you!")
println myCalculation("Fine")

//Reduction is possible
def combination = [id, length, numOfCapital, numOfSpaces, id].inject(id, myCombine)
println combination("Hi there! How are you doing?")
