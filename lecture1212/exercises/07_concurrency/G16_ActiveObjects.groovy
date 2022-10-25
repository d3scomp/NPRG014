import groovyx.gpars.actor.DynamicDispatchActor
import groovyx.gpars.activeobject.*
import org.codehaus.groovy.runtime.NullObject

@ActiveObject
class MyCounter extends DynamicDispatchActor {
    private int counter = 0

    @ActiveMethod
    def incrementBy(int value) {
        println "Received an integer: $value"
        this.counter += value
    }

    @ActiveMethod
    def update(String value) {
        println "Received a string: $value"
        this.counter += value.size()
    }

    @ActiveMethod
    def getValue(NullObject value) {
        return this.counter
    }
}

final MyCounter counter = new MyCounter()
counter.incrementBy 10
counter.incrementBy 20
counter.update 'Hello'
println counter.getValue().get()
assert 35 == counter.getValue().get()

println 'done'