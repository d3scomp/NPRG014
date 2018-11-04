package e06

/* Features:
 * - arbitrary operators
 * - infix notation (without a dot)
 * - additional constructor
 * - if-else is an expression
 * - requirements
 */

class Rational(n: Int, d: Int) {

	require(d != 0)

	private val g = gcd(n.abs, d.abs)

	val numer = n / g
	val denom = d / g

	def this(n: Int) = this(n, 1)

	def + (that: Rational) = new Rational(numer * that.denom + that.numer * denom, denom * that.denom)
	def + (i: Int) = new Rational(numer + i * denom, denom)
	def - (that: Rational) = new Rational(numer * that.denom - that.numer * denom, denom * that.denom)
	def - (i: Int) = new Rational(numer - i * denom, denom)
	def * (that: Rational) = new Rational(numer * that.numer, denom * that.denom)
	def * (i: Int) = new Rational(numer * i, denom)
	def / (i: Int) = new Rational(numer, denom * i)
	def / (that: Rational) = new Rational(numer * that.denom, denom * that.numer)
	def unary_- = new Rational(-numer, denom)

	override def toString = numer + "/" + denom
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}

object Rational {
	def apply(n:Int, d: Int) = new Rational(n, d)
	def apply(n: Int) = new Rational(n)
	
	
	def main(args: Array[String]) {
		val a = Rational(3, 5)
		val b = Rational(6, 7)

		val c = -a + b

		println(c)

		/* ASSIGNMENT 1
		 * Implement an operator that sets the numerator to a given value such that the following statement prints out "11/35"
		 * 
		 * println(c <-^ 11)
		 */

		/* ASSIGNMENT 2
		 * Implement a unary operator that inverts the rational number such that the following statement prints out "35/9"
		 * 
		 * println(!c)
		 */
		
		/* ASSIGNMENT 3
		 * Make it possible to prefix an integer number with R ~ to turn the number into a rational one. The following statement should print out "5/3"
		 * 
		 * println(R ~ 5/3)
		 */
	}
}