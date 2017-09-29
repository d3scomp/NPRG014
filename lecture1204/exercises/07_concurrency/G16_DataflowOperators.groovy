import groovyx.gpars.dataflow.DataflowQueue
import static groovyx.gpars.dataflow.Dataflow.operator

final DataflowQueue result = new DataflowQueue()
//TASK create necessary channels and encode the operator body to that we get a Fibonacci sequence on the output (the result channel)
final op = operator(...) { ... }


30.times {
    println result.val
}
op.stop()