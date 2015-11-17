import groovyx.gpars.actor.DynamicDispatchActor
import groovyx.gpars.activeobject.*
import org.codehaus.groovy.runtime.NullObject

//TODO Turn the following actor into an ActiveObject so that the script passes

class MyCounter extends DynamicDispatchActor {
    private int counter = 0

    def onMessage(int value) {
        println "Received an integer: $value"
        this.counter += value
    }

    def onMessage(String value) {
        println "Received a string: $value"
        this.counter += value.size()
    }

    def onMessage(NullObject value) {
        reply this.counter
    }
}

final MyCounter counter = new MyCounter()
counter.incrementBy 10
counter.incrementBy 20
counter.update 'Hello'
println counter.value
assert 35 == counter.value

println 'done'