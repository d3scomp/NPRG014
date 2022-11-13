package e20

import java.io.PrintWriter
import java.io.File

object ControlTest:

	def withPrintWriter(fileName: String)(op: PrintWriter => Unit): Unit =
		val writer = new PrintWriter(fileName)
		try
			op(writer)
		finally
			writer.close()

	def doNTimes(times: Int)(op: => Unit): Unit =
		for i <- 1 to times do
			op


	def main(args: Array[String]): Unit =
		withPrintWriter("out.txt") {
			writer =>
				doNTimes(5) {
					writer.println("Test")
					writer.println("Test2")
				}
		}

		/* ASSIGNMENT:
		 * Define a function ifThenElse which can be used as demonstrated below
 
		ifThenElse(2 > 1) { // should print "True branch"
			println("True branch")
		} {
			println("False branch")
		}
		
		val x = 2
		val y = 3
		val result = ifThenElse(x > y) { x } { y }
		println(result) // should print "3"

		*/




