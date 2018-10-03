abstract class Person {
    String name
}

class Man extends Person {
    public List marry(Woman partner) {
        [partner, this]
    }
}

class Woman extends Person {
    public List marry(Man partner) {
        [this, partner]
    }
}

final joe = new Man(name: 'Joe')
final dave = new Man(name: 'Dave')
final alice = new Woman(name: 'Alice')
final susan = new Woman(name: 'Susan')

assert [alice, joe] == joe.marry(alice)
assert [alice, joe] == alice.marry(joe)
assert [susan, dave] == susan.marry(dave)
assert [susan, dave] == dave.marry(susan)

//TASK check out the exception that gets thrown after uncommenting the following line
//assert [joe, dave] == joe.marry(dave)

//TASK override the + operator to marry people
//assert [alice, joe] == joe + alice
//assert [susan, dave] == susan + dave

println 'done'