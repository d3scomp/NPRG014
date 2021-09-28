import groovyx.gpars.dataflow.DataflowQueue
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.group.*


final DataflowQueue numbers = new DataflowQueue()
task {
    (1..11).each {
        numbers << it
    }
}
final DataflowQueue loopback = new DataflowQueue()
loopback << 1

final DataflowQueue result = new DataflowQueue()

operator(inputs: [numbers, loopback], outputs: [result, loopback]) {a, b ->
    bindAllOutputs(a*b)
}

10.times {
    println "Factorial of ${it+1}: ${result.val}"
}

println 'Done'