import groovyx.gpars.dataflow.Dataflow
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup

def radarOn = new DataflowVariable()

def checkEngine() {
    println "Checking the engine"
    sleep 3000
    return true
}

def checkTyres() {
    println "Preparing the tyres"
    sleep 4000
    return true
}

def engineCheck = task { checkEngine() }
def tyrePressure = task { checkTyres() }

task {
    println "Turning radar on"
    sleep 1000
    def result = System.currentTimeMillis() % 2 == 0 ? true : false
    radarOn << result
}

engineCheck.then {println "Engine ok"}
tyrePressure.then {println "Tyres ok"}
radarOn.then {println "Radar ${it ? 'ok' : 'failed'}"}

//TASK: Finish the transition of the code from using explicitly declared dataflow variables
//      to using Promise-returning functions by implementing a function for radar check.

boolean ready = [engineCheck, tyrePressure, radarOn].every {it.val}
if(ready) {
    println 'Taking off'
} else {
    println 'Staying on the ground today'
}