package e32

/* Features:
 * - type classes
 * - context bounds
 */

object StatisticsByTypeClass {

  trait NumberLike[T] {
    def plus(x: T, y: T): T
    def minus(x: T, y: T): T
    def multiply(x: T, y: T): T
    def divideByInt(x: T, y: Int): T
  }

  // Explicit declaration of implicit parameter
  def mean[T](xs: Vector[T])(implicit ev: NumberLike[T]): T =
    ev.divideByInt(xs.reduce(ev.plus(_, _)), xs.size)

  def median[T: NumberLike](xs: Vector[T]): T = xs(xs.size / 2)

  def quartiles[T: NumberLike](xs: Vector[T]): (T, T, T) =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  // Use of context bound and "implicitly" to simplify the declaration
  def iqr[T: NumberLike](xs: Vector[T]): T = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) =>
      implicitly[NumberLike[T]].minus(upperQuartile, lowerQuartile)
  }

  def meanOfSquares[T: NumberLike](xs: Vector[T]) =
    mean(xs.map(x => implicitly[NumberLike[T]].multiply(x, x)))

}

object StaticsByTypeClassBasicImplicits {
  import StatisticsByTypeClass.NumberLike

  implicit object NumberLikeDouble extends NumberLike[Double] {
    def plus(x: Double, y: Double) = x + y
    def minus(x: Double, y: Double) = x - y
    def multiply(x: Double, y: Double) = x * y
    def divideByInt(x: Double, y: Int) = x / y
  }

  implicit object NumberLikeInt extends NumberLike[Int] {
    def plus(x: Int, y: Int) = x + y
    def minus(x: Int, y: Int) = x - y
    def multiply(x: Int, y: Int) = x * y
    def divideByInt(x: Int, y: Int) = x / y
  }
}



class Duration(val totalSeconds: Double) {
  def this(min: Int, sec: Double) = this(min * 60 + sec)

  val minutes = (totalSeconds / 60).toInt
  val seconds = totalSeconds - minutes * 60

  override def toString = s"$minutes:$seconds"
}

object Duration {
  def apply(totalSeconds: Double) = new Duration(totalSeconds)
  def apply(min: Int, sec: Double) = new Duration(min, sec)
}


object StatisticsByTypeClassTest {

  def main(args: Array[String]): Unit = {
    import StatisticsByTypeClass._
    import StaticsByTypeClassBasicImplicits._

    println(mean(Vector(5, 10, 15))(NumberLikeInt))
    println(mean(Vector(5, 10, 15)))

    /* ASSIGNMENT
       Add a new type class implementation for the Duration such that the code below executes as expected.

    import StaticsByTypeClassDurationImplicits._
    println(mean(Vector(Duration(1, 0), Duration(1, 30), Duration(2,0))))
     */

    /* ASSIGNMENT
       Extend the declarations above to include method "meanOfSquares". It computes E(X^2).
       Use context bounds (as opposed to the explicit declaration of the implicit parameter) when implementing the
       mean of squares.

    println(meanOfSquares(Vector(1, 2, 3)))
    */
  }
}
