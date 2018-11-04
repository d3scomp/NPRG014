package e22

/* Features:
 * - tuples
 * - foldLeft
 */
object Queens {

	def main(args: Array[String]) {
		/* ASSIGNMENT:
		 * Add the missing piece into the method below. The method should compute the position
		 * of n queens on a chess board such that the queens do not endanger one another.
		 * The result for n=4 is the following:
		 *   queens(4) = List(
		 *     List((4,3), (3,1), (2,4) (1,2)),
		 *     List((4,2), (3,4), (2,1) (1,3))
		 *   )
		 */
		
		def queens(n: Int): List[List[(Int, Int)]] = {
			def isSafe(queen: Tuple2[Int, Int], queens: List[(Int, Int)]): Boolean = {
				!queens.exists {
					case (row, col) => queen._1 == row || queen._2 == col || queen._2 - queen._1 == col - row || queen._2 + queen._1 == col + row 
				}
			}

			// The output is list of placements, where a placement is a list of positions in form (row, column)
			def placeQueens(row: Int): List[List[(Int, Int)]] =
				if (row == 0) List(List()) else
				for {
					queens <- placeQueens(row - 1)
					// Add missing lines HERE
				} yield (0, 0) :: queens // Replace the (0, 0) with something appropriate  
				
			placeQueens(n)
		}
		
		val queensNo = 4
		val qnss = queens(queensNo)
		
		for (qns <- qnss)
			println("List(" + 
					qns.tail.foldLeft(qns.head.toString()) {(acc, qn) => acc + ", " + qn }
				+ ")")
		
		println()
		
		for (qns <- qnss) {
			for (qn <- qns) {
				println("-" * (qn._2 - 1) + "Q" + "-" * (queensNo - qn._2))
			}
			
			println()
		}
	}

}