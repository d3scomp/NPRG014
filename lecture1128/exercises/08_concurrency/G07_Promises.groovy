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

//TASK: Find out what the code does
//TASK: Radar can only be turned on after electricity is checked. Add an electricity-checking task and wire it so that radar is only turned on after electricity finishes correctly

boolean ready = [engineCheck, tyrePressure, radarOn].collect {it.val}.every{it}
if(ready) {
    println 'Taking off'
} else {
    println 'Staying on the ground today'
}