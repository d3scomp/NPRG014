import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.group.*


final DataflowQueue numbers = new DataflowQueue()

def alteredNumbers = numbers | {it * 2} | {it + 10}
def filtered = alteredNumbers.filter {it > 20}

//TASK: Negate all the numbers that come out of 'filtered' by adding another chained function

40.times {
    numbers << it
}

34.times {
    println filtered.val
}


println 'Done'
