package E03
import java.io.PrintStream


/* Features:
 * - braces not necessary if there is only single expression
 * - object defines a new class and acts as its singleton instance
 * - object is an instance thus it may be passed as a parameter
 * - class parameters, calling the superclass
 */

class Logger(where: PrintStream) {
	def log(msg: String) {
		where.println(msg)
	} 
}

/* ASSIGNMENT:
 * Change the AppLogger object line to print: ">> " + msg
 */
object AppLogger extends Logger(Console.out)



object LoggingExample {
	def doSomething(logger: Logger) {
		logger.log("I'm doing something")
	}
	
	def main(args: Array[String]) {
		AppLogger.log("Hello world")
		
		doSomething(AppLogger)
	}
}