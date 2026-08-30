final person = [:]
person.name = 'Joe'
person.age = 30
person.jump = {-> println 'Jumping'}
person.eat = {-> println 'Eating'}

println person
println person.name
person.jump()
person.eat()

//Select a method to call dynamically based on a string value in a variable
final action = 'jump'
person[action]()