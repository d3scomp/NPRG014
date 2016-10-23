package e13

/* Features:
 * - use of Scala concepts from Java
 * - lazy val
 */
trait Greeter {
	def saySomething(howManyTimes: Int)
	
	def saySomethingOnce() = saySomething(1)
}

class Hello(whom: String) extends Greeter {
	override def saySomething(howManyTimes: Int) {
		for (i <- 1 to howManyTimes) {
			println("Hello " + whom + "!")
		}	
	}
}

object Hello {
	private lazy val defaultGreeter = new Hello("World")
	
	def getDefaultGreeter = defaultGreeter
}

object InteropS {

	def main(args: Array[String]): Unit = {
		Hello.getDefaultGreeter.saySomething(3)
	}

}