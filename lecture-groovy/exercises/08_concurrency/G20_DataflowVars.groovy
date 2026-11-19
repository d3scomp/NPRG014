import java.util.concurrent.CompletableFuture

def x   = new CompletableFuture<Integer>()
def y   = new CompletableFuture<Integer>()
def add = new CompletableFuture<Integer>()
def mul = new CompletableFuture<Integer>()

Thread.startVirtualThread {
    add.complete(x.get() + y.get())
    println "Add: " + add.get()
}

Thread.startVirtualThread {
    mul.complete(x.get() * y.get())
    println "Multiply: " + mul.get()
}

Thread.startVirtualThread {
    println "Pretty printing"
    println "${x.get()} + ${y.get()} = ${add.get()}"
    println "${x.get()} * ${y.get()} = ${mul.get()}"
}

Thread.startVirtualThread {
    sleep(3000)
    x.complete(10)
}

Thread.startVirtualThread {
    sleep(2000)
    y.complete(4)
}

// Block until results are available
add.join()
mul.join()
sleep(1000)