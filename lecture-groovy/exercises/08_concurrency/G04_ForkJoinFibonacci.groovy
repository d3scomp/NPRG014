import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask

/**
 * The simplest possible parallel implementation of naive recursive Fibonacci number calculator.
 *
 * Using the standard Java ForkJoinPool + RecursiveTask, which is
 * demonstrated here.
 */

class FibTask extends RecursiveTask<Long> {
    final int n

    FibTask(int n) { this.n = n }

    @Override
    protected Long compute() {
        if (n <= 2) return 1L
        FibTask taskA = new FibTask(n - 2)
        taskA.fork()                       // schedule sub-task asynchronously
        FibTask taskB = new FibTask(n - 1)
        taskB.fork()        
        return taskB.join() + taskA.join()  // compute one branch directly, join the other
    }
}

final t1 = System.currentTimeMillis()
def numberOfThreads = 10
def pool = new ForkJoinPool(numberOfThreads)
try {
    println pool.invoke(new FibTask(32))
} finally {
    pool.shutdown()
}
println "Time: " + (System.currentTimeMillis() - t1)

//TODO Experiment with different sizes of the thread pool. What value gives the shortest calculation time on your system?
//TODO Explain why the algorithm works with fewer threads than there are tasks to calculate.