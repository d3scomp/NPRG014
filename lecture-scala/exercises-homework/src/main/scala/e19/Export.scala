package e19

class PosInt(value: Int):
  require(value > 0)

  export value.*
  // This makes the compiler generate methods like:
  // def +(x: Int): Int = value + x

  // One can also do some renaming or omit certain methods from the wildcard: export value.{<< as shl, >> as shr, >>> as _, *}


object Composition:
  def main(args: Array[String]): Unit =
    val x = PosInt(42)
    println(x + 1)

