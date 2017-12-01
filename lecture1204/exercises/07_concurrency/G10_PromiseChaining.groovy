import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.dataflow.Dataflow.*
import static groovyx.gpars.dataflow.Dataflow.whenAllBound

Closure download = { String url ->
    task {
        sleep 3000  //Simulate a web read
        return 'web content'
    }
}

Closure loadFile = { String fileName ->
    task {
        return 'file content'  //simulate a local file read
    }
}

Closure hash = { s -> s.hashCode() }

Closure compare = { int first, int second ->
    first == second
}

//TASK: Using the Dataflow.whenAllBound() and Promise.then() (aka pipe "|" or rightShift '>>') methods wire together multiple asynchronous functions
//      We needs hashes to be calculated on both web content and local file content and those hashes then need to be compared for equality
  
//TASK Block here until the whole calculation finishes
println 'Finished'