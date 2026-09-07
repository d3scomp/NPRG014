//TASK see and explain
trait Flying {
    void fly() {println "I am flying!"}
}

trait Quacking {
    void quack() {println "Quack!"}
}

class Duck implements Flying, Quacking {}

def duck = new Duck()

trait Named {
    String name
    void introduceYourself() {
        println "Hi, I'm $name"
    }
}

trait Loggable extends Named {
    void log(String message) {
        println("Log: origin=${Named.super.name}, message=$message")
    }
}

def loggedDuck = duck as Loggable
loggedDuck.name = "Kate"
loggedDuck.introduceYourself()

loggedDuck.log("Starting")
loggedDuck.fly()
loggedDuck.log("Landing")

//NOTE A proper class implementing the trait could have been used instead