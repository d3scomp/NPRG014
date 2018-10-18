class Counter {
    static long counter = 0
}

final threads = (1..50).collect {
    Thread.start {
        Counter.counter++
    }
}

threads*.join()
println Counter.counter

//TASK Properly synchronize
//TASK Replace thread creation with a thread pool (e.g. using java.util.concurrent.Executors.newFixedThreadPool(8))
