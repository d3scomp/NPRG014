import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.DefaultPGroup
import groovyx.gpars.group.PGroup


def group1 = new NonDaemonPGroup(4)
def group2 = new DefaultPGroup()
def stockPricesPRG = new DataflowQueue()
def stockPricesVIE = new DataflowQueue()
def stockPricesSTG = new DataflowQueue()
def avgPrices = new DataflowQueue()

def exit = new DataflowVariable()

def prg = group1.task {
    def random = new Random()
    10.times {
        sleep 1000
        stockPricesPRG << random.nextInt(10)
    }
}
def vie = group1.task {
    def random = new Random()
    10.times {
        sleep 1000
        stockPricesVIE << random.nextInt(10)
    }
}
def stg = group1.task {
    def random = new Random()
    10.times {
        sleep 1000
        stockPricesSTG << random.nextInt(10)
    }
}

def average = group2.operator(inputs: [stockPricesPRG, stockPricesVIE, stockPricesSTG], outputs: [avgPrices]) {p, v, s ->
        def avg = (p+v+s)/3
        println "Calculating the average price for: $p $v $s"
        bindOutput(0, avg)
}


//Handle progress
group2.task {
    def alt = group2.select(avgPrices, exit)
    while(true) {
        def msg = alt.prioritySelect()
        if (msg.index == 1) break
        if (msg.index == 0) {
            println "Current value of the stock: ${msg.value} EUR\n"
            sleep 1500  //Simulate the time penalty of a computation
        }
    }
}

[prg, vie, stg]*.join()
sleep 3000
exit << true
println 'Done'