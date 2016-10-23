package e05

/* Features
 * - companion object
 * - private default constructor
 * - val/var from class parameters (+ visibility)
 * - multiple parameters
 * - for comprehension
 * - apply method
 * - type parameters
 */

class MyList[T] private(private val item: T, private val next: MyList[T]) {

	override def toString(): String = {
		val result = new StringBuilder
		
		var list = this;
		while (list != null) {
			result.append(list.item.toString)
			
			if (list.next != null) {
				result.append(", ")
			}
			
			list = list.next
		}
		
		result.toString()
	}
}

object MyList {
	def apply[T](items: T*): MyList[T] = {
		var result: MyList[T] = null;
		
		for (item <- items.reverse) {
			result = new MyList[T](item, result)
		} 

		result
	} 
}

object CompanionExample {
	def main(args: Array[String]) {
		val list = MyList(1, 2, 3, 4, 5)
		
		println(list)
		
		/* ASSIGNMENT
		 * Extend the class MyList[T] so that the following line prints "1". 
		 * You don't have to consider any other inputs than 0.
		 * 
		 * println(list(0))
		 */
	}
	
}