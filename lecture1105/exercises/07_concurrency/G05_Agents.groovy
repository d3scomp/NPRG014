import groovyx.gpars.agent.Agent

def event = new Agent([])
Thread.start {
    event.send { it << 'Joe' }
    event.send { it << 'Dave' }
}

println "Instant peek: " + event.instantVal

Thread.start {
    event { it << 'Alice' }
    event { it << 'Susan' }
}

println "Another instant peek: " + event.instantVal
sleep 2000
println "Final state: " + event.val