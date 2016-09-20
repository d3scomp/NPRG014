import groovyx.gpars.dataflow.Dataflow
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup

def engineCheck = new DataflowVariable()
def tyrePressure = new DataflowVariable()
def radarOn = new DataflowVariable()

Thread.start {
    println "Checking the engine"
    sleep 3000
    engineCheck << true
    println "Engine ok"
}

Thread.start {
    println "Preparing the tyres"
    sleep 4000
    tyrePressure << true
    println "Tyres ok"
}

Thread.start {
    println "Turning radar on"
    sleep 1000
    def result = System.currentTimeMillis() % 2 == 0 ? true : false
    radarOn << result
    println "Radar ${result ? 'ok' : 'failed'}"
}

//TASK: Change the 'xxx ok' messages so that they are registerred as callbacks on the appropriate promises
//TASK: Include the actual true/false ('ok'/'failed') status of the check
//TASK: Use whenAllBound() to register a callback that prints the final 'Taking off'/'Staying on the ground today' message
//TIP: Dataflow.whenAllBound(){callback code here}
//TIP: whenAllBound() returns a Promise, Promises have a join() method

boolean ready = [engineCheck, tyrePressure, radarOn].collect {it.val}.every{it}
if(ready) {
    println 'Taking off'
} else {
    println 'Staying on the ground today'
}