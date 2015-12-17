package E02


/* Features:
 * - var & val
 * - primitive types are objects
 * - statically typed language
 * - type inference
 */
object StaticTypingMain {
	
	def printValue(value: Int) {
		println("Integer: " + value)
	}
	
	def printValue(value: Any) {
		println("Any: " + value)
	}
	
	def main(args: Array[String]) {
		var value: Any = "Hello world"
		printValue(value)
		
		/* ASSIGNMENT:
		 * Make the following statements print: "Integer: 3"
		 */
		value = 3
		printValue(value)
	
	
		val valueI = 5
		printValue(valueI)
	}
}