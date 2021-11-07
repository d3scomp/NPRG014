package e13

/* Features:
 * - for comprehensions with yield
 * - nested functions
 */
object ForComprehensions:

	def main(args: Array[String]): Unit =

		for i <- 1 to 10 do
			println(i)

		val filesHere = (new java.io.File("src/main/scala/e13")).listFiles

		def fileLines(file: java.io.File) = scala.io.Source.fromFile(file).getLines.toList

		val forLineLengths =
			for
				file <- filesHere
				if file.getName.endsWith(".scala")
				line <- fileLines(file)
				trimmed = line.trim
				if trimmed.matches(".*for.*")  
			yield file.toString() + ": " + trimmed.length
		
		for (line <- forLineLengths)
			println(line)

		println("----------------------------")

		val forLineLengths2 = filesHere.			
			withFilter { file => file.getName.endsWith(".scala") }.
			flatMap { file => fileLines(file).map(line => (file, line))	}.
			map { case (file, line) => (file, line, line.trim) }.
			withFilter { case (file, line, trimmed) => trimmed.matches(".*for.*") }.
			map { case (file, line, trimmed) => file.toString() + ": " + trimmed.length	}
		
		forLineLengths2.foreach {
			line => println(line)
		}
