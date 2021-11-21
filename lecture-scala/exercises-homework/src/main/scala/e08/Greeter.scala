package e08

/* Features:
 * - given and using
 * - given imports
 */

class PreferredPrompt(val preference: String)
class PreferredDrink(val preference: String)

object Greeter:
  def greet(name: String)(using prompt: PreferredPrompt,
                          drink: PreferredDrink) =

    println(s"Welcome, $name. The system is ready.")
    print("But while you work, ")
    println(s"why not enjoy a cup of ${drink.preference}?")
    println(prompt.preference)


object JoesPrefs:
  given prompt: PreferredPrompt =
    PreferredPrompt("relax> ")
  given drink: PreferredDrink =
    PreferredDrink("tea")

object JillsPrefs:
  given prompt: PreferredPrompt =
    PreferredPrompt("Your wish> ")
  given drink: PreferredDrink =
    PreferredDrink("cofee")


object GreeterTest:
  def main(args: Array[String]): Unit =
    // Note that import JillsPrefs.* does not automatically import givens they have to be imported by one of these:
    import JillsPrefs.given
    // import JillsPrefs.{prompt, drink}
    // import JillsPrefs.{given PreferredPrompt, given PreferredDrink}

    /* ASSIGNMENT 1
    * Change the declarations in this method to use the prompt from JoesPrefs. The drink should be water and it should
    * be defined directly in the main here.
    * The output should be:
    *   Welcome, Jill. The system is ready.
    *   But while you work, why not enjoy a cup of water?
    *   relax>
    */

    Greeter.greet("Jill")

