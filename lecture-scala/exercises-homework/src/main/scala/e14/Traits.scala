package e14

import scala.language.implicitConversions


/* Features:
 * - traits
 * - import of implicit defs in a function body
 */

trait Ordered[T]:
	def compare(that: T): Int

	def <(that: T) = (this compare that) < 0
	def >(that: T) = (this compare that) > 0
	def <=(that: T) = (this compare that) <= 0
	def >=(that: T) = (this compare that) >= 0


class Rational(n: Int, d: Int) extends Ordered[Rational]:
	require(d != 0)

	private val g = gcd(n.abs, d.abs)

	val numer = n / g
	val denom = d / g

	def this(n: Int) = this(n, 1)

	def + (that: Rational) = new Rational(numer * that.denom + that.numer * denom, denom * that.denom)
	def - (that: Rational) = new Rational(numer * that.denom - that.numer * denom, denom * that.denom)
	def * (that: Rational) = new Rational(numer * that.numer, denom * that.denom)
	def /~ (that: Rational) = new Rational(numer * that.denom, denom * that.numer)
	def unary_- = new Rational(-numer, denom)

	override def toString = numer + "/" + denom
	private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

	def compare(that: Rational) =
			(this.numer * that.denom) - (that.numer * this.denom)


object Rational:
	def apply(n:Int, d: Int) = new Rational(n, d)
	def apply(n: Int) = new Rational(n)

	given Conversion[Int, Rational] = new Rational(_)


object Traits:

	def main(args: Array[String]): Unit =
		import Rational.given
		
		val a = 2/~3 + 5/~6
		val b = 3/~7 + 7/~8

		println(a)
		println(b)

		println(a > b)
		println(a < b)		

		/* ASSIGNMENT:
		 * Introduce a new trait VerboseComparable that adds operations #<, #<=, #>, #>=, which return a string result as follows:
		 * 
		 * println(a #>= b) // 3/2 is more or equal to 73/56
		 * println(a #< b)	// 3/2 isn't strictly less than 73/56	
		 */
