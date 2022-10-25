import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.dataflow.*

/**
 * Demonstrates that deadlocks are deterministic in dataflow concurrency model. The deadlock appears reliably every time
 * the sample is run at exactly the same positions in code irrespective of the machine configuration.
 */

final def a = new DataflowVariable()
final def b = new DataflowVariable()

def t1 = task {
    println "Task 1 reading a"
    def v = a.get()
    println "Task 1 writing b"
    b << v + 1
}

def t2 = task {
    println "Task 2 reading b"
    def v = b.get()
    println "Task 2 writing a"
    a << v - 1
}

[t1, t2]*.join()