import groovyx.gpars.dataflow.Dataflow
import static groovyx.gpars.dataflow.Dataflow.*
import groovyx.gpars.dataflow.DataflowVariable

def x = new DataflowVariable()
def y = new DataflowVariable()
def add = new DataflowVariable()
def mul = new DataflowVariable()

task {
    add << x.get() + y.get()
    println "Add: " + add.get()
}

task {
    mul << x.get() * y.get()
    println "Multiply: " + mul.get()
}

task {
    println "Pretty printing"
    println "${x.get()} + ${y.get()} = ${add.get()}"
    println "${x.get()} * ${y.get()} = ${mul.get()}"
}

task {
    sleep 3000
    x << 10
}
task {
    sleep 2000
    y<<4
}

[add, mul]*.join()
sleep 1000