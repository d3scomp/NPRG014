import groovy.transform.ActiveMethod
import groovy.transform.ActiveObject

// TASK Investigate the effect of the @ActiveObject and @ActiveMethod annotations in the AST inspector

@ActiveObject
class MyCounter {
    private int counter = 0

    @ActiveMethod
    void incrementBy(int value) {
        println "Received an integer: $value"
        counter += value
    }

    @ActiveMethod
    void update(String value) {
        println "Received a string: $value"
        counter += value.size()
    }

    @ActiveMethod
    int getValue() {
        counter
    }

    @ActiveMethod(blocking=false)
    def getValueAsync() {
        counter
    }
}

def counter = new MyCounter()
counter.incrementBy(10)
counter.incrementBy(20)
counter.update('Hello')

assert counter.getValue() == 35
println counter.getValue()

println await counter.getValueAsync()
println 'done'