package e21

/* Features:
 * - multiline string
 * - custom string interpolator
 */

object JsonInterp:
	extension (sc: StringContext)
		def json(args: Any*): String =
			val strings = sc.parts.iterator
			val expressions = args.iterator

			var buf = new StringBuffer(strings.next)
			while strings.hasNext do
				buf.append(expressions.next match
					case arg: Int => arg
					case arg: Boolean => arg
					case arg: String => s"\"${arg.replaceAll("\"", "\\\\\"")}\""
				)

				buf.append(strings.next)

			buf.toString


	def main(args: Array[String]): Unit =
		val name = "John Doe"
		val age = 42
		val married = false

		// The following two declarations of the string are equivalent
		val jo = s"""{ "name": "$name", "age": $age, "married": $married }"""
		println(jo)

		val jo2 = StringContext("{ \"name\": \"", "\", \"age\": ", ", \"married\": ", " }").s(name, age, married)
		println(jo2)


		// Use of custom string interpolator
		val jo3 = json"""{ "name": $name, "age": $age, "married": $married }"""
		println(jo3)

		val nameWithMiddleName = """John "Marty" Doe"""
		val jo4 = json"""{ "name": $nameWithMiddleName, "age": $age, "married": $married }"""
		println(jo4)
