final person = [:]
person.name = 'Joe'
person.age = 30
person.jump = {-> println 'Jumping'}
person.eat = {-> println 'Eating'}

println person
println person.name
person.jump()
person.eat()

final action = 'jump'
person[action]()
