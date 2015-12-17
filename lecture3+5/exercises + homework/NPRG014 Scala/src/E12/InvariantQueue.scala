package E12

class InvariantQueue[/* + */ T] private (
		private var leading: List[T],
		private var trailing: List[T]
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
	
	def tail: InvariantQueue[T] = {
		mirror()
		new InvariantQueue(leading.tail, trailing)
	}
	
	def enqueue[U >: T](x: U) = new InvariantQueue[U](leading, x :: trailing)
}

