import groovy.transform.ToString

class Group {
    final members = [] as Set
    
    def leftShift(Person p) {
        members.add(p)
        return this
    }

    def plus(Group g) {
        members.addAll(g)
        return this
    }
}

@ToString
class Person {
    String name
    
    def plus(Person p) {
        final g = new Group()
        g << this
        g << p
        return g
    }    
}

final joe = new Person(name: 'Joe')
final dave = new Person(name: 'Dave')
final alice = new Person(name: 'Alice')
final susan = new Person(name: 'Susan')

assert [alice, joe] as Set == (joe + alice).members
assert [susan, dave] as Set == (susan + dave).members

final closeFriends = dave + susan
closeFriends << alice
closeFriends << joe
assert [susan, dave, joe, alice] as Set == (closeFriends).members

assert [alice, dave, susan, joe] as Set == ((alice + joe) << dave << susan).members


println 'done'