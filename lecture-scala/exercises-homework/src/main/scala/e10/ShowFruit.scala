/* Features
 * - C# style packaging vs. Java-style packaging (used in the rest of the examples)
 * - top-level function
 * - import via instance
 * - string interpolation
 */

package e10:
  package fruits:

    abstract class Fruit(
      val name: String,
      val color: String
    )

    object Fruits:
      object Apple extends Fruit("apple", "red")
      object Orange extends Fruit("orange", "orange")
      object Pear extends Fruit("pear", "yellowish")

      val menu = List(Apple, Orange, Pear)

    def showFruit(fruit: Fruit) =
      import fruit.*
      s"${name}s are $color"
