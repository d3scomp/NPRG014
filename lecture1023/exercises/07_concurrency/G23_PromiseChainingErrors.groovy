import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.dataflow.Dataflow.*
import static groovyx.gpars.dataflow.Dataflow.whenAllBound

Closure download = { String url ->
    sleep 3000  //Simulate a web read
    throw new RuntimeException('test')
}

Closure loadFile = { String fileName ->
     'file content'  //simulate a local file read
}

Closure hash = { s -> s.hashCode() }

Closure compare = { int first, int second ->
    first == second
}

//TASK: Handle the exception thrown from the web download task. Print out 'cannot tell' in case an exception occurs.
  
def all = whenAllBound([
        task({download('http://www.gpars.org')}).then(hash),
        task({loadFile('/coolStuff/gpars/website/index.html')}).then(hash)
], compare).then({ it ? 'identical' : 'different' })

println 'Result of comparison: ' + all.get()

println 'Finished'