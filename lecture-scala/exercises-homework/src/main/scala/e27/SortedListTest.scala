package e27

/* Features:
 * - type parameterization
 * - lower bound
 * - upper bound
 */

class A(val value: Int):
	def compare(that: A) = this.value - that.value


class B(value: Int) extends A(value)


class C(value: Int) extends A(value)


class SortedList[T <: A] private (private val list: List[T]):
	
	def this() = this(Nil)

	def head = list.head
	
	def tail = new SortedList[T](list.tail)

	def enlist[U >: T <: A](x: U): SortedList[U] =
		var result: List[U] = Nil
		var remaining = list
		var inserted = false
		
		while !remaining.isEmpty do
			if !inserted && (x.compare(remaining.head)) <= 0 then
				result = x :: result
				inserted = true

			result = remaining.head :: result
			remaining = remaining.tail

		if !inserted then
			result = x :: result

		new SortedList[U](result.reverse)


	def isEmpty = list.isEmpty


	def asListStatic[U >: T <: A]: SortedList[U] =
		if isEmpty then
			new SortedList[U]
		else
			new SortedList[U](head :: tail.asListStatic[U].list)


	def asListDynamic[U <: A]: SortedList[U] =
		if isEmpty then
			new SortedList[U]
		else
			val x: U = head.asInstanceOf[U]
			new SortedList[U](x :: tail.asListDynamic[U].list)


object SortedListTest:
	
	def main(args: Array[String]): Unit =
		val list1 = new SortedList[C]
		
		var list2 = list1.enlist(new C(4))

		val list3 = list2.enlist(new B(3))
		

		val list4 = list2.asListStatic[A]

		val list5 = new SortedList[A].enlist(new C(5)).asListDynamic[C]
		
		
		/* QUESTION
		 * Why do the lines 1-2 of the following snippet work?
		 * 
		 * val list6 = list5.asListDynamic[B]
		 * println(list6.head)
		 * val head = list6.head
		 */
