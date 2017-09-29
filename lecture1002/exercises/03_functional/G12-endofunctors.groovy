//All functions are String -> Boolean
Closure<Boolean> id = {String value -> true}
Closure<Boolean> empty = {String value -> value==null || value.size()==0 ? true : false}
Closure<Boolean> shortMsg = {String value -> value==null || value.size()<30 ? true : false}
Closure<Boolean> interestingMsg = {String value -> value!=null && value.contains('you') ? true : false}

Closure<Closure<Boolean>> andCombine = {Closure <Boolean> f, Closure <Boolean> g ->
    return {String value -> return f(value) && g(value)}
}

def myMsg = andCombine(shortMsg, interestingMsg)
println myMsg("Hello, how are you!")
println myMsg("Fine")

//Reduction is possible
def combination = [id, empty, shortMsg, interestingMsg, id, empty, shortMsg].inject(id, andCombine)
println combination("a")

//TASK Implement a monoidic 'orCombine' function that performs logical or operation. What is the id function?
//TASK Think about creating a monoid for functions String -> Integer