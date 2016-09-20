import groovy.transform.Immutable
import groovyx.gpars.actor.DynamicDispatchActor

//TODO Implement the actor class so that the script passes
class MyCounter extends DynamicDispatchActor {
}

final MyCounter counter = new MyCounter().start()
counter 10
counter 20
counter 'Hello'
assert 35 == counter.sendAndWait(new GetRequest())

//TODO Uncomment the following lines to see how mesage handlers can be added dynamically at runtime
//counter.when {Double value -> println "Received a double $value"}
//counter 1.0

@Immutable final class GetRequest {}

println 'done'