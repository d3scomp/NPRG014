package e29

/* Features:
 * - covariance with internal state
 */

object CovariantQueue:
	def apply() = new CovariantQueue(Nil, Nil)

class CovariantQueue[+T] private (
		private[this] var leading: List[T],
		private[this] var trailing: List[T]
	):

	private def mirror(): Unit =
		// The compiler checks that all accesses to the mutable state are within a single instance only
		if leading.isEmpty then
			leading = trailing.reverse
			trailing = Nil

	def head: T =
		mirror()
		leading.head
	
	def tail: CovariantQueue[T] =
		mirror()
		new CovariantQueue(leading.tail, trailing)
	
	def enqueue[U >: T](x: U) = new CovariantQueue[U](leading, x :: trailing)

