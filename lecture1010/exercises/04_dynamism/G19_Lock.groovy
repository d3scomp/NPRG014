import java.util.concurrent.locks.ReentrantLock

//In concurrent programms it is very important to protect access to shared mutable data. Locks is one way to prevent race conditions on share data,
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
//when evaluating the supplied piece of code and that would safely unlock the data once the block of code finishes.


sum = 0

threads = (0..50).collect {num ->
    Thread.start {
        lock.withLock {
            //'Holding the lock'
            sum += num
        }
        //'Not holding the lock'
    }
}
threads*.join()
assert 1275 == sum

println 'done'