final person = [:]
person.name = 'Joe'
person.age = 300

person.sprint = {println "I am $age years old and I am running in ${Thread.currentThread().name}"}
person.sprint.delegate = person
person.run = {sprint()}
person.run.delegate = person

println('Main thread: ' + Thread.currentThread().name)
person.sprint()
new Thread(person as Runnable).start()

sleep 1000