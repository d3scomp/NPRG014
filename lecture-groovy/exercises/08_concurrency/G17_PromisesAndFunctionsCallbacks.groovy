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



import groovy.concurrent.AsyncScope

import groovy.concurrent.AsyncScope
import groovy.concurrent.Awaitable

def runChecksWithCallbacks() {
    AsyncScope.withScope { scope ->
        // Run this exactly once; both callbacks depend on it.
        def electricity = scope.async {
            checkElectricity()
        }

        def engineCheck = electricity
            .thenCompose { powered ->
                if (powered) {
                    scope.async { checkEngine() }
                } else {
                    println 'Electricity failed; engine not started'
                    Awaitable.of(false)
                }
            }
            .then { ok ->
                println "Engine ${ok ? 'ok' : 'failed'}"
                ok
            }

        def tyrePressure = scope.async {
            checkTyres()
        }.then { ok ->
            println "Tyres ${ok ? 'ok' : 'failed'}"
            ok
        }

        def radarOn = electricity
            .thenCompose { powered ->
                if (powered) {
                    println 'Electricity ok, continue with the radar'
                    scope.async { checkRadar() }
                } else {
                    println 'Electricity failed; do not continue with radar'
                    Awaitable.of(false)
                }
            }
            .then { ok ->
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

runChecksWithCallbacks()

// TASK Currently, checkElectricity() is local to the checkRadar() function. Modify the code is a way
// that allows both checkRadar() and checkEngine() to first have the electricity checked and only after
// a successful result of electricity check the radar or engine can be turned on, respectively.
// Make sure that electricity is checked only once.