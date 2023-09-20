class TreasureBox {
    
    def treasure = "golden rings"
    
    def key1=false
    def key2=false
    def key3=false
            
    synchronized def enterPrezidentKey(key) {
        if (key == "1122") {
           key1 = true
           println "Unlocking with the president key"
           Thread.sleep(1000)
           this.notify()           
        }
    }

    synchronized def enterPrimeMinisterKey(key) {
        if (key == "1133") {
           key2 = true
           println "Unlocking with the prime minister key"
           Thread.sleep(1000)           
           this.notify()           
        }
    }

    synchronized def enterPoliceDirectorKey(key) {
        if (key == "1144") {
           key3 = true        
           println "Unlocking with the police director key"
           Thread.sleep(1000)           
           this.notify()
        }
    }
    
    synchronized def open() {
       while(!key1) this.wait() 
       while(!key2) this.wait()
       while(!key3) this.wait()              
       println "Opening the box and showing " + this.treasure
    }
}

def box = new TreasureBox()

Thread.start {
    box.open()
}

// TASK Uncomment and run. Why is the box opening reported only once? What is the term to describe issues like this?
// Fix the TreasureBox class so that it correctly opens three times, once for each calling thread.
//Thread.start {
//    box.open()
//}
//Thread.start {
//    box.open()
//}

box.enterPoliceDirectorKey("1144")
box.enterPrezidentKey("1122")
box.enterPrimeMinisterKey("invalid")
box.enterPrimeMinisterKey("1133")