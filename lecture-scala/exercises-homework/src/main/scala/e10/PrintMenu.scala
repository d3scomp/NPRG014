/* Features
 * - C# style packaging vs. Java-style packaging (used in the rest of the examples)
 * - relative imports
 */

package e10:
  package printmenu:
    import fruits.Fruits
    import fruits.showFruit
    // Package _root_ can be used to construct absolute paths

    object PrintMenu:
        def main(args: Array[String]) =
          println(
            for fruit <- Fruits.menu yield
              showFruit(fruit)
          )