package e06

/* Features:
 * - arbitrary operators
 * - infix notation (without a dot)
 * - additional constructor
 * - if-else is an expression
 * - requirements
 * - callable classes (i.e. apply automatically generated in the companion object)
 * - infix non-symbolic names and infix keyword
 */

class Rational(n: Int, d: Int):
	require(d != 0)

	private val g = gcd(n.abs, d.abs)

	val numer = n / g
	val denom = d / g

	def this(n: Int) = this(n, 1)

	def + (that: Rational) = Rational(numer * that.denom + that.numer * denom, denom * that.denom)
	def + (i: Int) = Rational(numer + i * denom, denom)
	def - (that: Rational) = Rational(numer * that.denom - that.numer * denom, denom * that.denom)
	def - (i: Int) = Rational(numer - i * denom, denom)
	def * (that: Rational) = Rational(numer * that.numer, denom * that.denom)
	def * (i: Int) = Rational(numer * i, denom)
	def / (i: Int) = Rational(numer, denom * i)
	def / (that: Rational) = Rational(numer * that.denom, denom * that.numer)
	def unary_- = Rational(-numer, denom)

	// In future versions of Scala, methods with non-symbolic names will only be allowed as
	// operators if they are declared with the infix modifier.
	infix def withDenom(i: Int) = Rational(numer, i)

	override def toString = s"${numer}/${denom}"
	private def gcd(a: Int, b: Int): Int = if b == 0 then a else gcd(b, a % b)


object RationalTest:
	def main(args: Array[String]): Unit =
		val a = Rational(3, 5)
		val b = Rational(6, 7)

		val c = -a + b

		val d = a + 5

		println(c)

		println(c withDenom 9)

		/* ASSIGNMENT 1
		 * Implement an operator that sets the numerator to a given value
		 * such that the following statement prints out "11/35"
		 *
		 * println(c <-^ 11)
		 */

		/* ASSIGNMENT 2
		 * Implement a unary operator ! that inverts the rational
		 * number such that the following statement prints out "35/9"
		 *
		 * println(!c)
		 */


		/* ASSIGNMENT 3
		 * Make is possible to prefix an integer number with R ~ to
		 * turn the number into a rational one. The following statement
		 * should print out "5/3"
		 *
		 * println(R ~ 5/3)
		 */
