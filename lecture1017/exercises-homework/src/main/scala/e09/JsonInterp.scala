package e09
import net.liftweb.json._

/* Features:
 * - standard string interpolator
 * - multiline string
 * - custom string interpolator
 * - implicit class
 */

object JsonImplicits {
	implicit class JsonHelper(val sc: StringContext) extends AnyVal {
		def json(args: Any*): JValue = {
			val strings = sc.parts.iterator
			val expressions = args.iterator

			var buf = new StringBuffer(strings.next)
			while(strings.hasNext) {
				buf append expressions.next
				buf append strings.next
			}

			parse(buf.toString)
		}
	}
}

object JsonInterp {
	import JsonImplicits._

	def main(args: Array[String]) {
		val name = "John Doe"
		val age = 42
		val married = false

		val jo = json"""{ "name": "$name", "age": $age, "married": $married }"""

		println(s"Name: ${(jo \ "name").values}")
		println(s"Age: ${(jo \ "age").values}")
		println(s"Married: ${(jo \ "married").values}")

		println()

		println(pretty(render(jo)))

		println()

		/* ASSIGNMENT:
		Implement another string interpolator, such that that following statements print this:
		{
			"name":"John Doe",
			"age":42,
			"married":false
		}

		val jo2 = jsaq"""{ "name": $name, "age": $age, "married": $married }"""
		println(pretty(render(jo2)))
		*/
	}
}
