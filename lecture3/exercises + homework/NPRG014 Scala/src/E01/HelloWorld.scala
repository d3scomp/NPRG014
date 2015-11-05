package E01

/* Features:
 * - no static members, but singleton object
 * - methods defined using def
 * - public visibility is default
 * - types are written after arguments
 * - semicolon inference
 */
object HelloWorld {
	def main(args: Array[String]) {
		println("Hello world")
	}
}