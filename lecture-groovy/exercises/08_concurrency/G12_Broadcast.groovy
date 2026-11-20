import groovy.concurrent.BroadcastChannel
import groovy.concurrent.Awaitable

// With BroadcastChannel all readers read all messages

def broadcast = BroadcastChannel.create()
def sub1 = broadcast.subscribe()
def sub2 = broadcast.subscribe()

async {
    broadcast.send('hello')
    broadcast.send('world')
    broadcast.close()
}

// Both subscribers receive both messages
def p1 = async {
    for await (msg in sub1) { println "Sub1: $msg" }
}

def p2 = async {
    for await (msg in sub2) { println "Sub2: $msg" }
}

await Awaitable.all(p1, p2)
println 'done'