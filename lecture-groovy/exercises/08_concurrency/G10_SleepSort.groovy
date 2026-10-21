import java.util.concurrent.CompletableFuture

final numbers = [10, 5, 59, 28, 31, 43, 21, 52, 9, 34, 38, 47, 48, 12, 17, 24, 15, 35, 51]

// CompletableFuture acts identically to GPars' DataflowVariable
final startSignal = new CompletableFuture<Integer>()

final threads = numbers.collect { num ->
    Thread.startVirtualThread {
        // .get() blocks the lightweight thread until the promise is fulfilled
        final coef = startSignal.get()
        sleep(num * coef)
        println num
    }
}

// Fulfills the promise and broadcasts the value to all waiting threads
startSignal.complete(50)

threads*.join()
println "Done"