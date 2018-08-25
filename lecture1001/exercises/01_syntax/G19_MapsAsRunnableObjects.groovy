final person = [:]
person.name = 'Joe'
person.age = 30

//TASK Make the following code pass. You may like to check the Runnable interface definition for hints.
person.sprint = {println "I am running as a teenager although I am $age years old."}
person.sprint.delegate = person

new Thread(person as Runnable).start()

sleep 1000