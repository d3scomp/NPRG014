package e15

/* Features:
 * - linearization
 * - combination of several traits
 */
class Animal:
	println("Animal initialized")

	def eat(): Unit =
		println("Animal.eat")


trait WarmBlooded extends Animal :
	println("WarmBlooded initialized")

	override def eat(): Unit =
		println("WarmBlooded.eat")
		super.eat()


trait Furry extends Animal with WarmBlooded:
	println("Furry initialized")

	override def eat(): Unit =
		println("Furry.eat")
		super.eat()


trait HasLegs extends Animal with WarmBlooded:
	println("HasLegs initialized")

	override def eat(): Unit =
		println("HasLegs.eat")
		super.eat()


trait FourLegged extends HasLegs:
	println("FourLegged initialized")
	
	override def eat(): Unit =
		println("FourLegged.eat")
		super.eat()


class Cat extends Animal with Furry with FourLegged:
	println("Cat initialized")

	override def eat(): Unit =
		println("Cat.eat")
		super.eat()


object Linearization:

	def main(args: Array[String]): Unit =
		val c = new Cat
		
		/* ASSIGNMENT
		 * Add a new trait called XYZ and update the program such that the linearization sequence will be Cat.eat, FourLegged.eat, HasLegs.eat, XYZ.eat, Furry.eat, Animal.eat
		 */
		c.eat()
