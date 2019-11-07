import groovyx.gpars.agent.Agent

def event = new Agent([])
Thread.start {
    event.send { it << 'Joe' }
    event.send { it << 'Dave' }
}

println "Instant peek: " + event.instantVal

//Notice syntax sugar here allowing you to omit "send"
Thread.start {
    event { it << 'Alice' }
    event { it << 'Susan' }
}

println "Another instant peek: " + event.instantVal
sleep 2000
event { it << 'Eve' }
println "Final state: " + event.val