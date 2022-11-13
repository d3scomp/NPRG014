package e24

/* Features:
 * - extractors
 * - infix extractors
 */

object EMail:
  def apply(name: String, domain: String) = name + "@" + domain

  def unapply(email: String) =
    val parts = email split "@"

    if parts.length == 2 then
      Some(parts(0), parts(1))
    else
      None

object DomainRev:
  def apply(dc: String*) = dc.reverse mkString "."
  def unapplySeq(domain: String) = Some(domain.split("\\.").toSeq.reverse)

object `@`:
  def unapply(email: String) = EMail.unapply(email)


object ExtractorTest:

  def validateEmail(email: String): Unit =
    println(email + " " + (
        email match
          case EMail(_, "d3s.mff.cuni.cz") => "is a department address address"
          case EMail(_, DomainRev("cz", "cuni", _*)) => "is a university address"
          case EMail(_, _) => "is a private address"
          case _ => "is in a bad format"
      )
    )

  def main(args: Array[String]): Unit =
    validateEmail(EMail("bures", "d3s.mff.cuni.cz"))
    validateEmail(EMail("doe", "xyz.natur.cuni.cz"))
    validateEmail("XXX")

    val name `@` DomainRev(dc1, dc2, _*) = "bures@d3s.mff.cuni.cz"
    println(s"$name @ ... . $dc2 . $dc1")
