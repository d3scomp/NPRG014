@BaseScript(MyBaseClass)
import groovy.transform.BaseScript

abstract class MyBaseClass extends Script {
    String name
    void greet() {
        println "Hello $name"
    }
    void say(String word) {
        println "I am saying $word"
    }
}

name = 'Ingrid'
greet()
say 'Hi'
say 'Bye'
