package e32

object StatisticsByAdapter {

  trait NumberLike[A] {
    def get: A

    def plus(y: NumberLike[A]): NumberLike[A]

    def minus(y: NumberLike[A]): NumberLike[A]

    def divide(y: Int): NumberLike[A]
  }

  type Quartile[A] = (NumberLike[A], NumberLike[A], NumberLike[A])

  def median[A](xs: Vector[NumberLike[A]]): NumberLike[A] = xs(xs.size / 2)

  def quartiles[A](xs: Vector[NumberLike[A]]): Quartile[A] =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[A](xs: Vector[NumberLike[A]]): NumberLike[A] = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => upperQuartile.minus(lowerQuartile)
  }

  def mean[A](xs: Vector[NumberLike[A]]): NumberLike[A] =
    xs.reduce(_.plus(_)).divide(xs.size)
}

object StaticsByAdapterBasicImplicits {
  import StatisticsByAdapter.NumberLike

  case class NumberLikeDouble(x: Double) extends NumberLike[Double] {
    def get: Double = x
    def minus(y: NumberLike[Double]) = NumberLikeDouble(x - y.get)
    def plus(y: NumberLike[Double]) = NumberLikeDouble(x + y.get)
    def divide(y: Int) = NumberLikeDouble(x / y)
  }

  implicit def doubleToNumberLike(v: Double) = NumberLikeDouble(v)
}

object StatisticsByAdapterTest {

  def main(args: Array[String]): Unit = {
    import StatisticsByAdapter._
    import StaticsByAdapterBasicImplicits._

    println(mean(Vector(NumberLikeDouble(5), NumberLikeDouble(10), NumberLikeDouble(15))))
    println(mean(Vector[NumberLike[Double]](5.0, 10.0, 15.0)))

  }
}

