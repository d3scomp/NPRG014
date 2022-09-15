final out = new StringWriter()
def builder = new groovy.xml.MarkupBuilder(out)

drivers = ['Joe', 'Alice', 'Dave', 'John', 'Susan']

final doc = builder.race() {
    drivers.eachWithIndex {name, index ->
        car(number: index) {
            driver(name: name)
        }
    }
}

println out.toString()