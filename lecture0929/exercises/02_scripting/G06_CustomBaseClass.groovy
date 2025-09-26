@BaseScript(MyBaseClass)
import groovy.transform.BaseScript

abstract class MyBaseClass extends Script {
    String myName
    void greet() {
        println "Hello $myName"
    }
    void say(String word) {
        println "I am saying $word"
    }
}

myName = 'Ingrid'
greet()
say 'Hi'
say 'Bye'