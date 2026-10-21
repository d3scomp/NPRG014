import groovy.concurrent.Actor
import org.apache.groovy.runtime.async.AsyncSupport
import java.util.concurrent.atomic.AtomicInteger

record GetRequest() {}

def total = new AtomicInteger(0)

def counter = Actor.reactor { message ->
    switch (message) {
        case Integer:
            println "Received an integer: $message"
            total.addAndGet(message)
            return null

        case String:
            println "Received a string: $message"
            total.addAndGet(message.size())
            return null

        case GetRequest:
            return total.get()

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