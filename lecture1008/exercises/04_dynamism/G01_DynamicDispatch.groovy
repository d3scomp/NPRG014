def calculate(String value) {
    value.size()
}

def calculate(Integer value) {
    value * 10
}

def a = 'Hello'
println 'First attempt: ' + calculate(a)
a = 5
println 'Second attempt: ' + calculate(a)

//Static type-cast will not work
//println calculate((Integer)a)

if (System.currentTimeMillis() % 2 == 0) a = 'Hi there!'
println 'Third attempt: ' + calculate(a)