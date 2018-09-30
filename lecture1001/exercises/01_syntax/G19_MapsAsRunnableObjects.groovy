final person = [:]
person.name = 'Joe'
person.age = 300

//TASK Make the following code pass. You may like to check the Runnable interface definition for hints.
person.sprint = {println "I am $age years old and I am running in ${Thread.currentThread().name}"}
person.sprint.delegate = person
person.run = {delegate.sprint()}
person.run.delegate = person

println('Main thread: ' + Thread.currentThread().name)
person.sprint()
new Thread(person as Runnable).start()

sleep 1000