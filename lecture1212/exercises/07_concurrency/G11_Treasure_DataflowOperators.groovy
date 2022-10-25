import groovyx.gpars.dataflow.DataflowQueue
import static groovyx.gpars.dataflow.Dataflow.operator
import static groovyx.gpars.dataflow.Dataflow.task

class TreasureBox {
    
    def treasure = "golden rings"
    
    def key1=new DataflowQueue()
    def key2=new DataflowQueue()
    def key3=new DataflowQueue()
    def output=new DataflowQueue()    
            
    def lock = operator(inputs:[key1, key2, key3], outputs: [output]) {k1, k2, k3 ->
        if (k1 == "1122" && k2 == "1133" && k3 == "1144") {
            bindAllOutputs(this.treasure)
        } else {
            bindAllOutputs("nothing")
        }
    }        
    
    def enterPrezidentKey(key) {
        key1 << key
        println "Unlocking with the president key"
        Thread.sleep(1000)
    }

    def enterPrimeMinisterKey(key) {
        key2 << key    
        println "Unlocking with the prime minister key"
        Thread.sleep(1000)           
    }

    def enterPoliceDirectorKey(key) {
        key3 << key    
        println "Unlocking with the police director key"
        Thread.sleep(1000)           
    }
    
    def open() {
       println output.val
    }
}

def box = new TreasureBox()

Thread.start {
    box.open()
}

box.enterPoliceDirectorKey("1144")
box.enterPrezidentKey("1122")
box.enterPrimeMinisterKey("invalid")

Thread.start {
    box.open()
}

box.enterPoliceDirectorKey("1144")
box.enterPrezidentKey("1122")
box.enterPrimeMinisterKey("1133")