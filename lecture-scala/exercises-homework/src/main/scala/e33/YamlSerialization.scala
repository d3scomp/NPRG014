package e33

def indentTail(value: String): String =
  val lines = value.split("\n").toList
  (lines.head :: lines.tail.map(x => s"  $x")).mkString("\n")


trait YamlSerializer[T]:
  def serialize(obj: T): String

  extension (x: T)
    def toYaml: String = serialize(x)

object YamlSerializer:
  given stringSerializer: YamlSerializer[String] with
    def serialize(s: String) = s

  given listSerializer[T](using YamlSerializer[T]): YamlSerializer[List[T]] with
    def serialize(lst: List[T]) =
      val lines = for entry <- lst yield
        val value = summon[YamlSerializer[T]].toYaml(entry)
        s"- ${indentTail(value)}"

      lines.mkString("\n")

class Person(val firstName: String, val lastName: String)

object Person:
  given YamlSerializer[Person] with
    def serialize(p: Person) =
      import YamlSerializer.given
      s"""firstName: ${p.firstName.toYaml}
         |lastName: ${p.lastName.toYaml}""".stripMargin


object YamlSerializerTest:
  def main(args: Array[String]): Unit =
    import YamlSerializer.given
    val a = "Hello"
    println(a.toYaml)

    val b1 = List("ab", "cd")
    val b2 = List("ef", "gh")
    println(b1.toYaml)

    val c = List(b1, b2)
    println(c.toYaml)

    val p1 = Person("John", "Doe")
    val p2 = Person("Jane", "X")
    println(p1.toYaml)

    val d = List(p1, p2)
    println(d.toYaml)
