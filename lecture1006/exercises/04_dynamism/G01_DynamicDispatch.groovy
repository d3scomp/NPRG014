def calculate(String value) {
    value * 10
}

def calculate(Integer value) {
    value * 10
}

def a = 'Hello'
println 'First attempt: ' + calculate(a)
a = 5
println 'Second attempt: ' + calculate(a)

println 'Third attempt: ' + calculate("12015" as Integer)

if (System.currentTimeMillis() % 2 == 0) a = 'Hi there!'
println 'Fourth attempt: ' + calculate(a)