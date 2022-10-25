import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.dataflow.Dataflow.*
import static groovyx.gpars.dataflow.Dataflow.whenAllBound

Closure download = { String url ->
    sleep 3000  //Simulate a web read
    return 'web content'
}

Closure loadFile = { String fileName ->
    return 'file content'  //simulate a local file read
}

Closure hash = { s -> s.hashCode() }

Closure compare = { int first, int second ->
    first == second
}

// Synchronous
println compare(hash(download("")), hash(loadFile("")))

// Asynchronous and blocking
def h1 = task {hash(download(""))}
def h2 = task {hash(loadFile(""))}
def p1 = task {compare(h1.get(), h2.get())}
println p1.get()

// Asynchronous and non-blocking (reactive)
def p2 = task {download("")} | hash
def p3 = task {loadFile("")} | hash
def p4 = whenAllBound(p2, p3, compare)
println p4.get()

println 'Finished'