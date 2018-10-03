final slave = new Slave()
slave.with {


    create gold
    mine silver
    prepare breakfast, lunch, dinner


}
//TASK use scripting to supply custom commands provided at run-time
String myCode = '''
create house
move furniture
'''


class Slave {
    def propertyMissing(String name) {
        "*${name.toUpperCase()}*"
    }

    def methodMissing(String name, args) {
        println "${name[0].toUpperCase() + name[1..-2]}ing ${args.join(', ')} as requested"
    }
}