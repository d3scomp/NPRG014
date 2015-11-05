package E14

class Food
class Fish extends Food
class Grass extends Food

abstract class Animal {
	type SuitableFood <: Food
	def eat(food: SuitableFood)
}

class Cow extends Animal {
	type SuitableFood = Grass

	override def eat(food: Grass) {}
}

object AbstractTypesTest {
	def main(args: Array[String]) {

		val bessy: Animal = new Cow
		
		bessy.asInstanceOf[Cow].eat(new Grass)

		// bessy.eat(new Fish) -- does not compile

		bessy.eat((new Grass).asInstanceOf[bessy.SuitableFood])

		bessy.eat((new Fish).asInstanceOf[bessy.SuitableFood]) // throws class cast exception
	}
}