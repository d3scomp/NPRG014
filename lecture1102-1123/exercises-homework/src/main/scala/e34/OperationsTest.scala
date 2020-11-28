package e34

import Operations._

object OperationsTest {

  def main(args: Array[String]): Unit = {
    var i = 2
    var j = 5

    def printState() = println(s"i=$i j=$j")

    printState()
    increment(i)
    printState()

    /* ASSIGNMENT
    Extend the Operations object to provide macro swap. It takes two integer variables and swaps their content.
    Swap should not generate any code if the two variables are the same - i.e. swap(i,i)

    swap(i, i)
    printState()
    swap(i, j)
    printState()
*/

  }
}
