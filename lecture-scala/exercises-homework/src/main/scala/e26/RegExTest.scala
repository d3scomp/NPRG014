package e26

import scala.util.matching.Regex

/* Features:
 * - regular expressions
 * - use of regular expressions as extractors
 * - combination of string interpolation with extractors
 */

object RegExTest:
  def test1(): Unit =
    val emailRegex = "([^@]+)@(.*)".r

    val emailRegex(name, domain) = "bures@d3s.mff.cuni.cz"

    println(s"1) Email is $name @ $domain")


  extension (sc: StringContext)
    // If the string interpolation function is parameterless, it is expected that it returns an object which can take
    // parameters, i.e. apply and/or unapply (depending on whether it is used RHS or LHS)
    def rr: Regex = new Regex(sc.parts.mkString)

  def test2a(): Unit =
    val rr"([^@]+)$name@(.*)$domain" = "bures@d3s.mff.cuni.cz"

    println(s"2a) Email is $name @ $domain")

  def test2b(): Unit =
    val regex = StringContext("([^@]+)", "@(.*)").rr
    val regex(name, domain) = "bures@d3s.mff.cuni.cz"

    println(s"2b) Email is $name @ $domain")


  def main(args: Array[String]): Unit =
    test1()
    test2a()
    test2b()
