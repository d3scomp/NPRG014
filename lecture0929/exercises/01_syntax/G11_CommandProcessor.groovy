class Plane {
    private int thrust = 0
    private boolean inFlight = false
    private boolean engineOn = false
    private int altitude = 0

    private void startEngine() {
        println 'Starting the engine'
        engineOn = true
    }

    private void stopEngine() {
        println 'Stopping the engine'
        engineOn = false
    }

    private void adjustThrust(int value) {
        thrust = value
        println 'Adjusting thrust to ' + thrust
    }

    private void climbTo(int altitude) {
        println "Changing altitude to $altitude"
        this.altitude = altitude
    }
}

final takeoff = {
    if (!engineOn) startEngine()
    adjustThrust 10
    climbTo 5000
    adjustThrust 5
}

final land = {
    adjustThrust 3
    climbTo 0
    adjustThrust 0
    stopEngine()
}

/**
 * The "command processor" pattern lets you treat a block of behaviour (a closure) as a first-class
 * value that can be stored, passed around, and executed later against a specific target object.
 *
 * Real-world examples:
 * • A database transaction manager that runs a user-supplied SQL block inside a begin/commit pair.
 * • A GUI framework that invokes a closure whenever a button is clicked.
 * • A scheduler that executes a task-closure at a given time on a worker object.
 *
 * In Groovy you can declare the expected type of a closure's delegate by typing the parameter with
 * `Closure<TargetType>`.  This gives the compiler (and the reader) a clear contract about which
 * methods the closure is allowed to call:
 *
 *   void performCommand(String name, Closure<Plane> command) {
 *       command.delegate = this
 *       // ...
 *   }
 *
 * The generic type argument is mostly a documentation hint — Groovy closures resolve method calls
 * dynamically — but it makes the intended delegate type explicit and is respected by IDEs and
 * static analysis tools.
 */
//TASK Implement the suggested performCommand() method to set the delegate so that the following code passes
final plane = new Plane()
plane.performCommand('Take off', takeoff)
println '*** We are in flight now ***'
plane.performCommand('Land', land)

//TASK Make the following code pass, too. Use the call() method to handle function calls on the Plane objects.
//plane('Take off', takeoff)
//println '*** We are in flight now ***'
//plane('Land', land)