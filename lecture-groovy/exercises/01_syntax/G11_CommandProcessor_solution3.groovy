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
    
    void performCommand(desc, @DelegatesTo(Plane) command) {
        // rehydrate(owner, thisObject, delegate) creates a copy of this closure with
        // new values for owner, thisObject ("it"), and delegate.
        //
        // Why do we need it?
        // When a closure is defined outside a class, its delegate defaults to the
        // script or class in which it was created (i.e., not "this" Plane).
        // Without rehydrate, calls like startEngine() or adjustThrust() inside the
        // closure would look up the wrong delegate and fail.
        //
        // By rehydrating with "this" as delegate, we tell the closure:
        //   - delegate  = this Plane instance  → method calls like startEngine()
        //                                     resolve against this Plane.
        //   - owner     = this Plane instance  → keeps the closure "inside" the
        //                                     Plane's scope.
        //   - thisObject= this Plane instance  → "it" inside the closure refers to
        //                                     this Plane.
        //
        // In short: rehydrate reparents the closure so that all its method calls
        // land on the current Plane instance instead of on "main".
        def code = command.rehydrate(this, this, this)
        code.call()
    }
    
    void call(desc, command) {
        performCommand(desc, command)
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

//TASK Implement the suggested performCommand() method to set the delegate so that the following code passes
final plane = new Plane()
plane.performCommand('Take off', takeoff)
println '*** We are in flight now ***'
plane.performCommand('Land', land)

//TASK Make the following code pass, too. Use the call() method to handle function calls on the Plane objects.
plane('Take off', takeoff)
println '*** We are in flight now ***'
plane('Land', land)