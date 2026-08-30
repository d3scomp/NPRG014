/*
 * This script highlights three powerful Groovy metaprogramming features:
 * 
 * 1. Map-Based Objects: We are building an object dynamically using a simple Map (person).
 * 
 * 2. Closure Delegation: By setting the `delegate` property of the `sprint` and `run` 
 *    closures to the `person` map, the closures can resolve variables they don't own 
 *    (like `$age` and the `sprint()` method call) by looking them up in the map.
 * 
 * 3. Map-to-Interface Coercion: Using the `as` keyword (`person as Runnable`), Groovy 
 *    dynamically casts the Map to a Java Interface. When the Thread calls the Runnable's 
 *    `run()` method, Groovy automatically executes the closure stored in the map under 
 *    the matching `run` key.
 * 
 * The output proves the closures can safely execute across both the main thread 
 * and a newly spawned background thread.
 */
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