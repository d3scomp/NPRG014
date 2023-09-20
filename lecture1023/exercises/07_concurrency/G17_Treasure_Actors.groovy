// SOLUTION Reimplement the TreasureBox class using the actor abstraction
import groovy.transform.Immutable
import groovyx.gpars.actor.DynamicDispatchActor

@Immutable class UseKey {
    String key
}
@Immutable class KeyInserted {
    int holderIndex
}
@Immutable class OpenBoxRequest {}
@Immutable class BoxContent {
    String treasure
}

class KeyHolder extends DynamicDispatchActor {
    int holderIndex
    TreasureBox protectedBox
    String key
    
    def onMessage(UseKey e) {
        println "Unlocking with the key for index $holderIndex"
        Thread.sleep(1000)               
        if (e.key == this.key) {
            protectedBox << new KeyInserted(holderIndex)
        }
    }
}

class TreasureBox extends DynamicDispatchActor {

    private def treasure = "golden rings"
    
    final keys = [false, false, false]

    def onMessage(KeyInserted e) {
        keys[e.holderIndex] = true
        println "Box unlocked for key ${e.holderIndex}"        
    }
            
    def onMessage(OpenBoxRequest e) {
        println "Trying to open the box now"
        if (keys.inject(true) {a, b -> a && b}) {
            reply new BoxContent(treasure: this.treasure)
        } else {
            reply "Nothing here"
        }
    }
}

def box = new TreasureBox()
def prezident = new KeyHolder(holderIndex: 0, protectedBox: box, key: '1122')
def primeMinister = new KeyHolder(holderIndex: 1, protectedBox: box, key: '1133')
def policeDirector = new KeyHolder(holderIndex: 2, protectedBox: box, key: '1144')

box.start()
prezident.start()
primeMinister.start()
policeDirector.start()


    println 'Attempt 1: ' + box.sendAndWait(new OpenBoxRequest())


policeDirector << new UseKey(key: "1144")
prezident << new UseKey(key: "1122")
primeMinister << new UseKey(key: "invalid")


    println 'Attempt 2: ' + box.sendAndWait(new OpenBoxRequest())


primeMinister << new UseKey(key: "1133")

Thread.sleep(5000)

def t = Thread.start {
    println box.sendAndWait(new OpenBoxRequest())
}

t.join()
println 'Done'
