import groovyx.gpars.GParsPool
import static groovyx.gpars.GParsPool.withPool

/**
 * The simplest possible parallel implementation of naive recursive Fibonacci number calculator
 */

final t1 = System.currentTimeMillis()
withPool() {
    println GParsPool.runForkJoin(32) { n ->
        if (n <= 2) return 1
        forkOffChild(n - 2)
        forkOffChild(n - 1)
        getChildrenResults().sum()
    }
}
println "Time: " + (System.currentTimeMillis() - t1)

//TODO Experiment with different sizes of the thread pool. What value gives the shortest calculation time on your system?
//TODO Explain why the algorithm works with fewer threads than there are tasks to calculate.