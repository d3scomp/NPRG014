package e32.by_typeclass

/* Features:
 * - type classes
 * - context bounds
 */
object StatisticsByTypeClass:
  trait NumberLike[T]:
    def plus(x: T, y: T): T
    def minus(x: T, y: T): T
    def divideByInt(t: T, i: Int): T

    extension(x: T)
      def +(y: T): T = plus(x, y)
      def -(y: T): T = minus(x, y)
      def /(y: Int): T = divideByInt(x, y)

  // Explicit declaration of given parameter
  def mean[T](xs: Array[T])(using ev: NumberLike[T]): T =
    xs.reduce(_ + _) / xs.size

  // Shorthand syntax - so call context bound (“a context parameter that depends on a type parameter”)
  def median[T: NumberLike](xs: Array[T]): T = xs(xs.size / 2)

  def quartiles[T: NumberLike](xs: Array[T]): (T, T, T) =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[T: NumberLike](xs: Array[T]): T = quartiles(xs) match
    case (lowerQuartile, _, upperQuartile) =>
      upperQuartile - lowerQuartile


  given NumberLike[Double] with
    def plus(x: Double, y: Double) = x + y
    def minus(x: Double, y: Double) = x - y
    def divideByInt(x: Double, y: Int) = x / y

  given NumberLike[Int] with
    def plus(x: Int, y: Int) = x + y
    def minus(x: Int, y: Int) = x - y
    def divideByInt(x: Int, y: Int) = x / y


class Duration(val totalSeconds: Double):
  def this(min: Int, sec: Double) = this(min * 60 + sec)

  val minutes = (totalSeconds / 60).toInt
  val seconds = totalSeconds - minutes * 60

  override def toString = s"$minutes:$seconds"


object Duration:
  def apply(totalSeconds: Double) = new Duration(totalSeconds)
  def apply(min: Int, sec: Double) = new Duration(min, sec)




object StatisticsByTypeClassTest:

  def main(args: Array[String]): Unit =
    import StatisticsByTypeClass.*

    println(mean(Array(5, 10, 15)))

    /* ASSIGNMENT
       Add a new type class implementation for the Duration such that the code below executes as expected.

    import StaticsByTypeClassDurationImplicits._
    println(mean(Vector(Duration(1, 0), Duration(1, 30), Duration(2,0))))
     */

