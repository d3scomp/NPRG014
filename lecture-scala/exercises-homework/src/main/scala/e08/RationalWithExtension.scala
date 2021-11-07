package e08

/* Features:
 * - extensions
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

	override def toString = s"${numer}/${denom}"
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)


object RationalOps:
	object R:
		def ~(i: Int) = Rational(i)

	extension (lhs: Int)
		def ~ (dummy: R.type) = Rational(lhs)


object RationalWithExtensionTest:
	def main(args: Array[String]): Unit =
		import RationalOps.*

		val a = Rational(3, 5)
		val b = Rational(6, 7)

		val c = -a + b
		println(c)

		println(R ~ 5/3)

		println(2 ~ R)

		/* ASSIGNMENT
		 * Introduce necessary definitions in order to make the statement
		 * below possible. It should print out "1/2"
		 *
		 * println(1/2 ~ R)
		 */
