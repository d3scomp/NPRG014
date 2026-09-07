import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup


def group = new NonDaemonPGroup()
def stockPrices = new DataflowQueue()
def exchangeRates = new DataflowQueue()
def exchangeClosed = new DataflowVariable()

group.task {
    def random = new Random()
    10.times {
        sleep 1000
        stockPrices << random.nextInt(10)
    }
    exchangeClosed << true
}

group.task {
        exchangeRates << 25
        sleep 3000
        exchangeRates << 24
        sleep 3000
        exchangeRates << 23                
}


//Handle progress
group.task {
    def alt = group.select(exchangeRates, stockPrices, exchangeClosed)
    int currentRate = 0
    while(true) {
        def msg = alt.prioritySelect()
        if (msg.index == 2) break  // The Stock Exchange has just been closed
        if (msg.index == 0) {
            // The CZK/EUR rate has just changed
            currentRate = msg.value
            println "Exchange rate changed to ${currentRate} CZK/EUR"
        } else {
            // New stock price has arrived
            println "Current value of the stock ${msg.value} EUR, which is ${msg.value * currentRate} CZK"
        }
    }
}.join()

println "Finished"