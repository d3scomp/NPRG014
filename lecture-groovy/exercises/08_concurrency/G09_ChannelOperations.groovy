import groovy.concurrent.AsyncChannel

def numbers = AsyncChannel.create()

def filtered = numbers
    .map { it * 2 }
    .map { it + 10 }
    .filter { it > 20 }
    .map { -it }

async {
    40.times {
        await numbers.send(it)
    }
    numbers.close()
}

for await (value in filtered) {
    println value
}

println 'Done'