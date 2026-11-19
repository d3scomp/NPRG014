import groovy.concurrent.AsyncScope

def checkEngine() {
    println 'Checking the engine'
    sleep 3000
    true
}

def checkTyres() {
    println 'Preparing the tyres'
    sleep 4000
    true
}

def checkElectricity() {
    println 'Checking electricity'
    sleep 3000
    System.currentTimeMillis() % 3 == 0
}

def checkRadar() {
    println 'Turning radar on'
    sleep 1000
    sleep 500
    System.currentTimeMillis() % 2 == 0
}


def runChecks() {
    AsyncScope.withScope { scope ->
        def engineCheck = scope.async {
            boolean ok

            if (checkElectricity()) {
                ok = checkEngine()
            } else {
                println 'Electricity failed; engine not started'
                ok = false
            }

            println "Engine ${ok ? 'ok' : 'failed'}"
            ok
        }

        def tyrePressure = scope.async {
            boolean ok = checkTyres()
            println "Tyres ${ok ? 'ok' : 'failed'}"
            ok
        }

        def radarOn = scope.async {
            boolean ok

            if (true) {  //TODO we need a real electricity check here
                println 'Electricity ok, continue with the radar'
                ok = checkRadar()
            } else {
                println 'Electricity failed; do not continue with radar'
                ok = false
            }

            println "Radar ${ok ? 'ok' : 'failed'}"
            ok
        }

        def (engineOk, tyresOk, radarOk) =
            await(engineCheck, tyrePressure, radarOn)

        if ([engineOk, tyresOk, radarOk].every { it }) {
            println 'Taking off'
        } else {
            println 'Staying on the ground today'
        }
    }
}

runChecks()

// TASK Currently, checkElectricity() is local to the checkRadar() function. Modify the code is a way
// that allows for electricity to be checked asynchronously and then both checkRadar() and checkEngine()
// should to first have the electricity checked and only after
// a successful result of electricity check the radar or engine can be turned on, respectively.
// Make sure that electricity is checked only once.