// SOLUTION using a CountDownLatch

class TreasureBox {
    
    def treasure = "golden rings"
    
    def key1=false
    def key2=false
    def key3=false
    def lock = new java.util.concurrent.CountDownLatch(3)
            
    def enterPrezidentKey(key) {
        if (key == "1122") {
           key1 = true
           lock.countDown()
           println "Unlocking with the president key"
           Thread.sleep(1000)
        }
    }

    def enterPrimeMinisterKey(key) {
        if (key == "1133") {
           key2 = true
           lock.countDown()           
           println "Unlocking with the prime minister key"
           Thread.sleep(1000)           
        }
    }

    def enterPoliceDirectorKey(key) {
        if (key == "1144") {
           key3 = true  
           lock.countDown()                 
           println "Unlocking with the police director key"
           Thread.sleep(1000)           
        }
    }
    
    def open() {
        lock.await()
       if (key1 && key2 && key3) {
           println "Opening the box and showing " + this.treasure
       } else {
           println "Box is locked"
       }
    }
}

def box = new TreasureBox()

Thread.start {
    box.open()
}

Thread.start {
    box.open()
}
Thread.start {
    box.open()
}

box.enterPoliceDirectorKey("1144")
box.enterPrezidentKey("1122")
box.enterPrimeMinisterKey("invalid")
box.enterPrimeMinisterKey("1133")