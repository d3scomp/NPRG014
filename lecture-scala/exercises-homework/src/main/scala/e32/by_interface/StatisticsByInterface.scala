package e32.by_interface

trait NumberLike[T]:
  def get: T
  def -(other: NumberLike[T]): NumberLike[T]
  def +(other: NumberLike[T]): NumberLike[T]
  def /(n: Int): NumberLike[T]


class Duration(val totalSeconds: Double) extends NumberLike[Duration]:
  def this(min: Int, sec: Double) = this(min * 60 + sec)

  val minutes = (totalSeconds / 60).toInt
  val seconds = totalSeconds - minutes * 60

  override def toString = s"$minutes:$seconds"

  def get = this

  def -(other: NumberLike[Duration]) = Duration(totalSeconds - other.get.totalSeconds)
  def +(other: NumberLike[Duration]) = Duration(totalSeconds + other.get.totalSeconds)
  def /(n: Int) = Duration(totalSeconds / n)

object Duration:
  def apply(totalSeconds: Double) = new Duration(totalSeconds)

  def apply(min: Int, sec: Double) = new Duration(min, sec)


class IntWithNumberLike(val x: Int) extends NumberLike[IntWithNumberLike]:
  def get = this
  def -(y: NumberLike[IntWithNumberLike]) = IntWithNumberLike(x - y.get.x)
  def +(y: NumberLike[IntWithNumberLike]) = IntWithNumberLike(x + y.get.x)
  def /(y: Int) = IntWithNumberLike(x / y)
  override def toString: String = x.toString


object StatisticsByInterface:
  type Quartile[A] = (A, A, A)

  def median[T](xs: Array[NumberLike[T]]): NumberLike[T] = xs(xs.size / 2)

  def quartiles[T](xs: Array[NumberLike[T]]): Quartile[NumberLike[T]] =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[T](xs: Array[NumberLike[T]]): NumberLike[T] = quartiles(xs) match
    case (lowerQuartile, _, upperQuartile) => upperQuartile - lowerQuartile

  def mean[T](xs: Array[NumberLike[T]]): NumberLike[T] =
    xs.reduce(_ + _) / xs.size


object StatisticsByInterfaceTest:

  def main(args: Array[String]): Unit =
    import StatisticsByInterface.*

    println(mean(Array(IntWithNumberLike(5), IntWithNumberLike(10), IntWithNumberLike(15))))
    println(mean(Array(Duration(0, 15), Duration(1, 30), Duration(2, 0))))
