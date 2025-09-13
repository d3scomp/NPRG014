import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup
import groovyx.gpars.dataflow.Dataflow
import groovyx.gpars.dataflow.*
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup

def businessClassPeople = new DataflowQueue()
def economyClassPeople = new DataflowQueue()
def emergencyCalls = new DataflowBroadcast()
def scheduledEvents = new DataflowBroadcast()
def beat = new DataflowQueue()

final START = 'Start'
final STOP = 'Stop'
final BEAT = 'Beat'

task {
    def myEvents = scheduledEvents.createReadChannel()
    def msg
    do {
        msg = myEvents.val
    } while (msg != START)

    while (!myEvents.isBound()) {
//        println 'Sending beat'
        sleep(1000)
        beat << BEAT
    } 
}

//Entrance
def e = task {
    def myEvents = scheduledEvents.createReadChannel()
    def myEmergencies = emergencyCalls.createReadChannel()
    def msg
    do {
        msg = myEvents.val
    } while (msg != START)
    
    def alt = select(myEvents, myEmergencies, beat)
    def random = new Random()
    int personCounter = 0
    SelectResult r
    while (r = alt.prioritySelect()) {
        switch(r.index) {
            case 0: if (r.value == STOP) println 'Entrance: Stopping';return;break;
            case 1: println 'Entrance: == Handling emergency ==';break;            
            case 2: def isBusiness = random.nextInt(0, 10) > 7
                    def person = (isBusiness) ? "Business class person" : "Economy class person"
                    person += ' ' + personCounter++
                    println "Entrance: new $person is comming"
                    if (isBusiness) businessClassPeople << person
                    else economyClassPeople << person
                    
        }
    }
}

//Checkin desk
def checkinDesk = {
    def myEvents = scheduledEvents.createReadChannel()
    def myEmergencies = emergencyCalls.createReadChannel()    
    def msg
    do {
        msg = myEvents.val
    } while (msg != START)
    
    def alt = select(myEvents, myEmergencies, businessClassPeople, economyClassPeople)
    SelectResult r
    while (r = alt.prioritySelect()) {
        switch(r.index) {
            case 0: if (r.value == STOP) println 'Checkin: Stopping';return;break;
            case 1: println 'Checkin: == Handling emergency ==';break;
            case 2: println 'Checkin: Processing ' + r.value;sleep(4000);break;
            case 3: println 'Checkin: Processing ' + r.value;sleep(2000);break;
        }
    }   
}

def c1 = task checkinDesk
//def c2 = task checkinDesk
//def c3 = task checkinDesk

println '*** Starting'
scheduledEvents << START

sleep 3000
emergencyCalls << 'emergency'

sleep 15000

println '*** Stopping'
scheduledEvents << STOP
[e, c1,]*.join()
//[e, c1, c2, c3]*.join()
println '*** Stopped'