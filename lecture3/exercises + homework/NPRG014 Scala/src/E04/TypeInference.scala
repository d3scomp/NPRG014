package E04


/* Features:
 * - types inferred automatically
 * - braces not necessary if there is only single expression
 */

abstract class Shape
abstract class Rectangular extends Shape
abstract class Oval extends Shape
class Rectangle extends Rectangular
class Square extends Rectangular
class Circle extends Oval
class Ellipse extends Oval


object TypeInference {

	/* ASSIGNMENT:
	 * Change the getShape declaration so that the application prints out "Drawing general shape"
	 */
	def getShape(id: Int) = 
		if (id == 0) {
			new Rectangle
		} else {
			new Square
		}
	
	def draw(shape: Rectangular) {
		println("Drawing rectangular shape") 
	}
	
	def draw(shape: Oval) {
		println("Drawing oval shape") 
	}
	
	def draw(shape: Shape) {
		println("Drawing general shape") 
	}
	
	def main(args: Array[String]) {
		val shape = getShape(1)
		
		draw(shape)
	}
}