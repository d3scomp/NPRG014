package e29

import scala.util.matching.Regex

/* Features:
 * - regular expressions
 * - use of regular expressions as extractors
 * - combination of string interpolation with extractors
 */

object RegExTest {
  def test1(): Unit = {
    val emailRegex = "([^@]+)@(.*)".r

    val emailRegex(name, domain) = "bures@d3s.mff.cuni.cz"

    println(s"Email is $name @ $domain")
  }


  implicit class RegexContext(sc: StringContext) {

    // If the string interpolation function is parameterless, it is expected that it return an object which can take
    // parameters, i.e. apply and/or unapply (depending on whether it is used RHS or LHS)
    def r = new Regex(sc.parts.mkString)
  }

  def test2(): Unit = {
    val r"([^@]+)$name@(.*)$domain" = "bures@d3s.mff.cuni.cz"

    println(s"Email is $name @ $domain")
  }

  def main(args: Array[String]): Unit = {
    test1()
    test2()
  }
}
