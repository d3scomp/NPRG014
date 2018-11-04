package e15

/* Features:
 * - private[this] visibility
 * - covariance
 */

class CovariantQueue[+T] private (
		private[this] var leading: List[T], 
		private[this] var trailing: List[T]
	) {

	private def mirror() {
		if (leading.isEmpty) {
			leading = trailing.reverse
			trailing = Nil
		}
	}
	
	def head: T = {
		mirror()
		leading.head
	}
	
	def tail: CovariantQueue[T] = {
		mirror()
		new CovariantQueue(leading.tail, trailing)
	}
	
	def enqueue[U >: T](x: U) = new CovariantQueue[U](leading, x :: trailing)
}
