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
    radarOn << true
    println "Radar ok"
}

//TASK: Radar can only be turned on after electricity is checked. Add an electricity-checking task
//and wire it so that the radar can only turned on after the electricity check finishes correctly.

boolean ready = [engineCheck, tyrePressure, radarOn].every {it.val}
if(ready) {
    println 'Taking off'
} else {
    println 'Staying on the ground today'
}