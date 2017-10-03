import java.util.concurrent.locks.ReentrantLock

//In concurrent programms it is very important to protect access to shared mutable data. Locks is one way to prevent race conditions on shared data,
//however, the expected try-catch-finally structure is a somewhat verbose and error-prone.
ReentrantLock lock = new ReentrantLock()

int sum = 0

threads = (0..50).collect {num ->
    Thread.start {
        lock.lock()
        try {
            //'Holding the lock'
            sum += num
        } finally {
            lock.unlock()
        }
        //'Not holding the lock'
    }
}
threads*.join()
assert 1275 == sum



//TASK Define a withLock method on the ReentrantLock class that would protect shared data from concurrent access
//when evaluating the supplied piece of code and that would safely unlock the data once the block of code finishes - either successfully or with an exception.

//START of your implementation of the withLock method

lock.metaClass.withLock = {code -> 
        code()
}

//END of your implementation of the withLock method


sum = 0

threads = (0..50).collect {num ->
    Thread.start {
        try {
            lock.withLock {
                //'Holding the lock'
                if (num==0) throw new RuntimeException("Things can fail sometimes. Be prepared!")
                sum += num
            }
            //'Not holding the lock'
        } catch(all) {
            println "The calculation for $num failed, but we go on."
        }
    }
}
threads*.join()
assert 1275 == sum

println 'done'