package e28

trait Func2P[O, I1, I2]:
	def execute(x1: I1, x2: I2): O


class B
class BA extends B
class BAA extends BA
class BB extends B
class BBA extends BB


object F extends Func2P[BAA, B, B]:
	def execute(x1: B, x2: B): BAA =
		new BAA


object FuncTest:

	def execute(f: Func2P[BA, BA, BB]): Unit =
		println(f.execute(new BA, new BB))
	
	def main(args: Array[String]): Unit = {

		/* ASSIGNMENT:
		 * Adjust the code above in such a way that the line below works.
		 * 
		 * execute(F)
		 */
	}