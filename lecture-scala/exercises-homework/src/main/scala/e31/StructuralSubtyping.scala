package e31

import reflect.Selectable.reflectiveSelectable

class Logger(printer : { def println(msg: String): Unit}):
	def log(msg: String): Unit =
		printer.println(msg)


object ConsoleLogger:
	def println(msg: String): Unit =
		Console.println(msg)


object StructuralSubtyping:
	
	def main(args: Array[String]): Unit =
		val logger = new Logger(ConsoleLogger)

		logger.log("Hello world!")
