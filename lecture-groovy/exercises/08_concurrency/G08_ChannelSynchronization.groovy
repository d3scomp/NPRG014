import groovy.concurrent.AsyncChannel

def numbers = AsyncChannel.create()
//def numbers = AsyncChannel.create(1)

async {
    println "Sending 1"
    def promise = numbers.send(1)
    println "Sent 1"
    await promise
    println "My message was consumed"
    numbers.close()
}

sleep(5000)
println "Reading..."
sleep(2000)
def value = await numbers.receive()
println "Read $value"

println 'Done'