package e26

/* Features:
 * - tuples
 * - foldLeft
 */
object Queens:

	def main(args: Array[String]): Unit =
		def queens(n: Int): List[List[(Int, Int)]] =
			def isSafe(queen: Tuple2[Int, Int], queens: List[(Int, Int)]): Boolean =
				!queens.exists {
					case (row, col) => queen(0) == row || queen(1) == col || queen(1) - queen(0) == col - row || queen(1) + queen(0) == col + row
				}

			def placeQueens(row: Int): List[List[(Int, Int)]] =
				if row == 0 then
					List(List())
				else
					for
						queens <- placeQueens(row - 1)
						column <- 1 to n
						queen = (row, column)
						if isSafe(queen, queens)
					yield queen :: queens
				
			placeQueens(n)
		
		val queensNo = 4
		val qnss = queens(queensNo)
		
		for (qns <- qnss) do
			println("List(" + 
					qns.tail.foldLeft(qns.head.toString()) {(acc, qn) => acc + ", " + qn }
				+ ")")
		
		println()
		
		for (qns <- qnss) do
			for (qn <- qns) do
				println("-" * (qn(1) - 1) + "Q" + "-" * (queensNo - qn(1)))
			
			println()
