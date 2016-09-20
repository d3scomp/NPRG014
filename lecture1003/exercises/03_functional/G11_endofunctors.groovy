//All functions are Integer -> Integer
def duplicate = {Integer value -> 2*value}
def triple = {Integer value -> 3*value}
def increment = {Integer value -> value + 1}
def addTwo = increment >> increment
def id = {it}

def functions = [duplicate, triple, increment, addTwo]

//TASK reduce all the above functions into one that is their combination
def combo = ...

println combo(10)
assert 63 == combo(10)