package e35

object PowTest:
  inline def power(inline x: Double, inline n: Int) =
    ${ PowMacro.powerCode('x, 'n)  }

  def main(args: Array[String]): Unit =
    println(power(3, 6))
