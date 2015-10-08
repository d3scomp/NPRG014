//example from www.groovy.cz

zdravim damy, slecny
zdravim pany









def methodMissing(String name, args) {
    println "$name ${args.join(', ')}"
}

def propertyMissing(String name) { name }

