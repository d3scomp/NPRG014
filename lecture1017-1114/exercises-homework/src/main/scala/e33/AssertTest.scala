package e33

import Assert._

object AssertTest {

  def main(args: Array[String]): Unit = {
    val a = 2

    assert(a == 0, "2 > 3")

    assert(a == 0)

  }
}
