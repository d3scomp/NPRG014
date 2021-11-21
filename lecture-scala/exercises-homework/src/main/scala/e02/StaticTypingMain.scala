package e02

/* Features:
 * - var & val
 * - primitive types are objects
 * - statically typed language
 * - type inference
 */
object StaticTypingMain:
	
	def printValue(value: Int): Unit =
		println("Integer: " + value)

	def printValue(value: Any): Unit =
		println("Any: " + value)

	def main(args: Array[String]): Unit =
		var value: Any = "Hello world"
		printValue(value)

		val valueI = 5

		printValue(valueI)

		value = 3
		printValue(value)

