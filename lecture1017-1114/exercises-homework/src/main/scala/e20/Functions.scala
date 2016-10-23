package e20

/* Features:
 * - anonymous functions and function parameters
 */
object Functions {

	def sort(xs: Array[Int]): Array[Int] = {
		if (xs.length <= 1) xs
		else {
			val pivot = xs(xs.length / 2)
			Array.concat(
					sort(xs filter (pivot >)), 
					xs filter (pivot ==), 
					sort(xs filter (pivot <)))
		}
	}

	
	def concatArray[T](items: Array[T], fcn: T => String) = {
		val bld = new StringBuilder
		
		for (item <- items) bld.append(fcn(item))
		bld.toString()	
	}
	
	def main(args: Array[String]) {
		
		val increase = (x: Int) => x + 9999

		println(increase(10))
		
		val someNumbers = -10 to 5
		println(someNumbers.filter(x => x > 0))

		println(someNumbers.filter(_ > 0))

		
		val otherNumbers = sort(Array(1,4,2,9,-1))
		
		val conc = (x: Int) => x + " "
		println(concatArray(otherNumbers, conc))
		
		def conc2(x: Int) = x + " "

		println(concatArray(otherNumbers, conc2))
		
		println(concatArray(otherNumbers, (_: Int) + " "))

		/* ASSIGNMENT:
		 * Write another sort function that will accept the comparison function, which takes two integers on input and returns one integer on output 
		 * (with the same semantics as the function compare in class Relational). Your sort function should use this compare method for comparison.
		 * It will be used in the following way:
		 * 
		 * val otherNumbers2 = sort(Array(1,4,2,9,-1), _ - _) 
		 */
		
	}

}