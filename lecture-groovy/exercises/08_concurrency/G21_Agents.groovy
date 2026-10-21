import groovy.concurrent.Agent

def event = Agent.create([])

Thread.start {
    event.send { it + 'Joe' }
    event.send { it + 'Dave' }
}

println "Instant peek: " + event.get()

Thread.start {
    event.send { it + 'Alice' }
    event.send { it + 'Susan' }
}

println "Another instant peek: " + event.get()

sleep 2000

event.send { it + 'Eve' }
println "Final state: " + await(event.getAsync())

event.shutdown()