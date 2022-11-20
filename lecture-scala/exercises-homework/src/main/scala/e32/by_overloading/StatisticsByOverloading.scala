package e32.by_overloading

import e32.by_typeclass.Duration

class Duration(val totalSeconds: Double):
  def this(min: Int, sec: Double) = this(min * 60 + sec)

  val minutes = (totalSeconds / 60).toInt
  val seconds = totalSeconds - minutes * 60

  override def toString = s"$minutes:$seconds"

  def -(other: Duration) = Duration(totalSeconds - other.totalSeconds)

  def +(other: Duration) = Duration(totalSeconds + other.totalSeconds)

  def /(n: Int) = Duration(totalSeconds / n)

object Duration:
  def apply(totalSeconds: Double) = new Duration(totalSeconds)

  def apply(min: Int, sec: Double) = new Duration(min, sec)


object StatisticsByOverloading:
  type Quartile[A] = (A, A, A)

  def median(xs: Array[Int]): Int = xs(xs.size / 2)
  def median(xs: Array[Duration]): Duration = xs(xs.size / 2)

  def quartiles(xs: Array[Int]): Quartile[Int] =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))
  def quartiles(xs: Array[Duration]): Quartile[Duration] =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr(xs: Array[Int]): Int = quartiles(xs) match
    case (lowerQuartile, _, upperQuartile) => upperQuartile - lowerQuartile
  def iqr(xs: Array[Duration]): Duration = quartiles(xs) match
    case (lowerQuartile, _, upperQuartile) => upperQuartile - lowerQuartile

  def mean(xs: Array[Int]): Int =
    xs.reduce(_ + _) / xs.size
  def mean(xs: Array[Duration]): Duration =
    xs.reduce(_ + _) / xs.size


object StatisticsByOverloadingTest:

  def main(args: Array[String]): Unit =
    import StatisticsByOverloading.*

    println(mean(Array(5, 10, 15)))
    println(mean(Array(Duration(0, 15), Duration(1, 30), Duration(2, 0))))
