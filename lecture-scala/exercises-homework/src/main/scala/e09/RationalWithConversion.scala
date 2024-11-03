package e09
import scala.language.implicitConversions

/* Features:
 * - conversions
 */

object R

class Rational(n: Int, d: Int):

	require(d != 0)

	private val g = gcd(n.abs, d.abs)

	val numer = n / g
	val denom = d / g

	def this(n: Int) = this(n, 1)

	def + (that: Rational) = Rational(numer * that.denom + that.numer * denom, denom * that.denom)
	def - (that: Rational) = Rational(numer * that.denom - that.numer * denom, denom * that.denom)
	def * (that: Rational) = Rational(numer * that.numer, denom * that.denom)
	def / (that: Rational) = Rational(numer * that.denom, denom * that.numer)
	def unary_- = new Rational(-numer, denom)

	override def toString = s"${numer}/${denom}"
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)


object Rational:
	given Conversion[Int, Rational] = i => new Rational(i)


object RationalWithConversionTest:
	def main(args: Array[String]): Unit =
		import Rational.given

		val c = 1 + Rational(1, 2) + 3

		println(c)
		
		/* ASSIGNMENT
		 * Introduce necessary definitions in order to make the statement 
		 * below possible. It should print out "3/2"
		 * 
		 * println(2/3 ~ R + 5/6 ~ R)
		 */
