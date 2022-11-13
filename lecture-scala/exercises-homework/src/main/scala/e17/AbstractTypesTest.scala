package e17

/* Features:
 * - abstract types
 * - overrides with specialized signatures
 */

class Food
class Fish extends Food
class Plants extends Food
class Grass extends Plants

abstract class Animal:
	outer =>

	type SuitableFood <: Food
	def eat(food: SuitableFood): Unit = {}

	def amountOfFoodPerDay: Double


class Shark(val amountOfFoodPerDay: Double) extends Animal :
	type SuitableFood = Fish


abstract class Herbivore extends Animal :
	// A subclass can restrict the abstract type to only a subtype. This is useful if it requires the abstract type
	// to have certain properties
	type SuitableFood <: Plants

class Cow(val amountOfFoodPerDay: Double) extends Herbivore :
	type SuitableFood = Grass


object Pasture:
	var animals: List[Animal { type SuitableFood = Grass }] = Nil


object AbstractTypesTest:
	def main(args: Array[String]): Unit =

		val bessy = new Cow(10)
		val sissy = new Cow(5)
		val shawn = new Shark(20)

		val animal: Animal = bessy

		val grass = new Grass

		bessy.eat(grass)

		// bessy.eat(new Fish) -- does not compile

		animal.eat((new Grass).asInstanceOf[animal.SuitableFood])
		// animal.eat((new Fish).asInstanceOf[animal.SuitableFood]) -- throws class cast exception

		/* ASSIGNMENT
		Add a new method to class Animal such that the following statements work as described

		println(bessy.eatsMoreThan(sissy))      -- prints "true"
		// println(bessy.eatsMoreThan(shawn))   -- does not compile
		*/


		Pasture.animals = bessy :: Pasture.animals
