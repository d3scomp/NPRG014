package e11

/* Features:
 * - linearization
 * - combination of several traits
 */
class Animal {
	println("Animal initialized")

	def eat() {
		println("Animal.eat")
	}
}

trait Furry extends Animal {
	println("Furry initialized")

	override def eat() {
		println("Furry.eat")
		super.eat()
	}
}

trait HasLegs extends Animal {
	println("HasLegs initialized")

	override def eat() {
		println("HasLegs.eat")
		super.eat()
	}
}

trait FourLegged extends HasLegs {
	println("FourLegged initialized")
	
	override def eat() {
		println("FourLegged.eat")
		super.eat()
	}
}

class Cat extends Furry with FourLegged {
	println("Cat initialized")

	override def eat() {
		println("Cat.eat")
		super.eat()
	}
}

object Linearization {

	def main(args: Array[String]) {
		val c = new Cat
		
		/* ASSIGNMENT
		 * Add a new trait called XYZ and update the program such that the linearization sequence will be Cat.eat, FourLegged.eat, HasLegs.eat, XYZ.eat, Furry.eat, Animal.eat
		 */
		c.eat()
	}
}