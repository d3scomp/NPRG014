import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup
import groovyx.gpars.dataflow.Dataflow
import static groovyx.gpars.dataflow.Dataflow.*

final numbers = [10, 5, 59, 28, 31, 43, 21, 52, 9, 34, 38, 47, 48, 12, 17, 24, 15, 35, 51]

final startSignal = new DataflowVariable()

final threads = numbers.collect { num ->
    task {
        final coef = startSignal.get()
        sleep num * coef
        println num
    }
}

startSignal << 50
threads*.join()
println "Done"
