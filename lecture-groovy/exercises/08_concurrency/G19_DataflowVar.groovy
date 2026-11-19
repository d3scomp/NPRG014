import java.util.concurrent.CompletableFuture

def x   = new CompletableFuture<Integer>()
def y   = new CompletableFuture<Integer>()
def signal   = new CompletableFuture<String>()

Thread.startVirtualThread {
    println "Value X is: " + x.get()
}

Thread.startVirtualThread {
    println "Value Y is: " + y.get()
}

Thread.startVirtualThread {
    println "Will start binding soon..."
    sleep(1000)
    println "Binding X: " + x.complete(35)
    sleep(1000)
    println "Binding Y: " + y.complete(12) 
    
    println "Stopping" 
    signal.complete("stop")  
}

signal.join()
println 'done'