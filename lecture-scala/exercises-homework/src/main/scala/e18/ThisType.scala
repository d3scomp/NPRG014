package e18

/* Features:
 * - this.type -- e.g. to allow function chaining
 * - applying trait during instantiation
 * - variables in trait
 */

class Person(var name: String, var age: Int):
  override def toString: String = s"${name} aged ${age}"


trait Annotation:
  var annotation: String = null
  def setAnnotation(annot: String): this.type =
    annotation = annot
    this

  override def toString: String =
    s"${super.toString} [${annotation}]"

// We could create a class that merges in the trait, but it can be also done directly as part of instantiation (see the main)
class PersonWithAnnotation(name: String, age: Int) extends Person(name, age) with Annotation

object ThisType {
  def main(args: Array[String]): Unit =
    val p = (new Person("John Doe", 42) with Annotation).setAnnotation("xyz")
    println(p)
    p.setAnnotation("abc")
    println(p)

}
