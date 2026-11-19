import groovy.concurrent.ChannelSelect
import groovy.concurrent.AsyncChannel

def random = new Random()

def prices = AsyncChannel.create(10)
def alerts = AsyncChannel.create(10)

async {
    sleep random.nextInt(1000)
    prices.send(500)
}

async {
    sleep random.nextInt(1000)
    alerts.send("Attention!")
}

def sel = ChannelSelect.from(prices, alerts)
def result = await sel.select()
println "Channel ${result.index}: ${result.value}"