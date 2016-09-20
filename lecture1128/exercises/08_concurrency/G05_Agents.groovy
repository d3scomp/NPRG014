import groovyx.gpars.agent.Agent

def event = new Agent([])
Thread.start {
    event { it << 'Joe' }
    event { it << 'Dave' }
}

println event.instantVal

Thread.start {
    event { it << 'Alice' }
    event { it << 'Susan' }
}

println event.instantVal
sleep 2000
println event.val
