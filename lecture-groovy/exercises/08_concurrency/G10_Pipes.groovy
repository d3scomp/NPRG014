/*
# Exercise: Build an asynchronous event router

**Time:** approximately 5 minutes  
**Technology:** Groovy 6 — `AsyncChannel`, `async`, `await`

### The scenario

Imagine two independent services producing log messages concurrently. Implement a small pipeline that:

1. Combines the two incoming channels.
2. Duplicates incoming messages into a monitoring channel.
3. Transforms the messages into appropriately labelled ones.
4. Waits for both producer tasks to finish.

Complete the `TODO` items.
*/

import groovy.concurrent.AsyncChannel
import groovy.concurrent.Awaitable

def service = AsyncChannel.create(10)
def audit   = AsyncChannel.create(10)
def monitor = AsyncChannel.create(2)

// Two concurrent producers
def serviceProducer = async {
    ['INFO: started', 'ERROR: disk full', 'INFO: ready'].each {
        await service.TODO(it)
    }
    service.close()
}

def auditProducer = async {
    ['WARN: retry', 'ERROR: timeout', 'INFO: login'].each {
        await audit.TODO(it)
    }
    audit.close()
}

// TODO 1: Combine the two input channels
def combined = service.TODO(audit)

// TODO 2: Copy messages into monitor, while passing them onward
def observed = combined.TODO(monitor)

// TODO 3: Filter ERROR messages
def errors = observed.TODO {
    it.startsWith('ERROR:')
}

// TODO 4: Transform ERROR messages - add a label prefix
def output = errors.TODO {
    "ALERT -> $it"
}

// Collect the processed messages
def rendered = []
def renderer = async {
    for (message in output) {
        rendered << message
    }
}

// Read the six copies sent to the monitoring channel
def auditTrail = (1..6).collect {
//    sleep 1000
    await monitor.receive()
}

// TODO 5: Wait for both producer tasks to finish
await Awaitable.all(serviceProducer, auditProducer, renderer)

// Verify the result
assert rendered.size() == 2
assert auditTrail.size() == 6

println rendered.sort()
println "Monitoring received ${auditTrail.size()} events."

/*

### Requirements

- Replace all six `TODO` items.
- The assertions must pass.

### Expected result

The output contains two error messages labelled `ALERT`. The monitoring channel receives all six original messages.

The order of the messages is intentionally unspecified because the producers run concurrently.

**Bonus question:** What would happen if the monitoring channel had insufficient buffer capacity and nobody consumed its messages?
                    Uncomment the commented out sleep to get a hint.
*/