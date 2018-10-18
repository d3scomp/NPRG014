import groovyx.gpars.dataflow.DataflowQueue
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.group.*


final DataflowQueue numbers = new DataflowQueue()
task {
    10.times {
        numbers << it+1
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
