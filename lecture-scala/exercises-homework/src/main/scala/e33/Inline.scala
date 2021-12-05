package e33

object InlineTest:

  inline def pow(x: Double, inline n: Int): Double =
    inline if n == 0 then
      1
    else inline if n == 1 then
      x
    else
      if n % 2 == 1 then
        pow(x * x, n / 2) * x
      else
        pow(x * x, n / 2)


  def main(args: Array[String]): Unit =
    val result0 = pow(3, 0)
    val result1 = pow(3, 1)
    val result2 = pow(3, 2)
    val result3 = pow(3, 3)
    val result4 = pow(3, 4)
    val result5 = pow(3, 5)

    println(result0)
    println(result1)
    println(result2)
    println(result3)
    println(result4)
    println(result5)