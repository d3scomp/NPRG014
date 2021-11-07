package e01

/* Features:
 * - no static members, but singleton object
 * - methods defined using def
 * - public visibility is default
 * - types are written after arguments
 * - semicolon inference
 * - block inference via indenting - braces optional, optional end
 */
object HelloWorld:
	def main(args: Array[String]): Unit =
		println("Hello world")

	// Alternative syntax for blocks (Scala version 2)
	def printHelloAll(): Unit = {
		println("Hello all")
	}

	// Scala version 3 with explicit block end
	def printHelloUniverse(): Unit =
		println("Hello universe")
	end printHelloUniverse





