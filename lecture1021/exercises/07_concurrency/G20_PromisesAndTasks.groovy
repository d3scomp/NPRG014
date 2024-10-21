import groovyx.gpars.dataflow.Dataflow
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup

def engineCheck = new DataflowVariable()
def tyrePressure = new DataflowVariable()
def radarOn = new DataflowVariable()

task {
    println "Checking the engine"
    sleep 3000
    engineCheck << true
    println "Engine ok"
}

task {
    println "Preparing the tyres"
    sleep 4000
    tyrePressure << true
    println "Tyres ok"
}

task {
    println "Turning radar on"
    sleep 1000
    def result = System.currentTimeMillis() % 2 == 0 ? true : false
    radarOn << result
    println "Radar ${result ? 'ok' : 'failed'}"
}
//TASK: Radar can only be turned on after electricity is checked. Add an electricity-checking task
//and wire it so that the radar can only turned on after the electricity check finishes correctly.

//TASK: Change the 'xxx ok' messages so that they are registered as callbacks on the appropriate promises

boolean ready = [engineCheck, tyrePressure, radarOn].every {it.val}
if(ready) {
    println 'Taking off'
} else {
    println 'Staying on the ground today'
}