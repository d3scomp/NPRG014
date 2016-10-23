package e08

/* Features:
 * - symbol literals and their comparison
 */

object Literals {
  def main(args: Array[String]): Unit = {
    val s = new RequestS(new String("POST")) // "POST" would in most cases work because JVM usually (but not always) interns strings
    val y = new RequestY(Symbol("POST")) // Symbol is always interned

    s.execute()
    y.execute()
  }
}