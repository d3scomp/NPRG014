// TASK Reimplement the TreasureBox class using the agent abstraction
import groovyx.gpars.agent.Agent
import groovyx.gpars.group.*

class Content {
    private def treasure = "golden rings"
    
    String toString() {
           if (key1 && key2 && key3) {
               return this.treasure
           } else {
               return "Box is locked"
           }   
    }
    
    def key1=false
    def key2=false
    def key3=false
}

class TreasureBox {
    def state = new Agent(new Content())
            
    def enterPrezidentKey(key) {
        if (key == "1122") {
           state << {
               println "Unlocking with the president key"
               Thread.sleep(1000) 
               it.key1 = true
           }           
        }
    }

    def enterPrimeMinisterKey(key) {
        if (key == "1133") {
           state << {
               println "Unlocking with the prime minister key"
               Thread.sleep(1000)           
               it.key2 = true
           }
        }
    }

    def enterPoliceDirectorKey(key) {
        if (key == "1144") {
           state << {
               println "Unlocking with the police director key"
               Thread.sleep(1000)           
               it.key3 = true
           }     
        }
    }
    
    def open() {
       println state.val
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

box.enterPrimeMinisterKey("1133")

Thread.start {
    box.open()
}

def t = Thread.start {
    box.open()
}

t.join()
println 'Done'
