import groovy.concurrent.Actor
import org.apache.groovy.runtime.async.AsyncSupport

record GetRequest() {}

def counter = Actor.stateful(0) { state, message ->
    switch (message) {
        case Integer:
            println "Received an integer: $message"
            return state + message

        case String:
            println "Received a string: $message"
            return state + message.size()

        case GetRequest:
            return state

        default:
            throw new IllegalArgumentException(
                "Unsupported message: $message"
            )
    }
}

counter.send(10)
counter.send(20)
counter.send('Hello')

assert 35 == AsyncSupport.await(
    counter.sendAndGet(new GetRequest())
)

counter.stop()
println 'done'