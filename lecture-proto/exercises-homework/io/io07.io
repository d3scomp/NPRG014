Object methodClosure := method(methodName,
	closure := Object clone
	closure __method := self getSlot(methodName)

	for (idx, 1, call argCount - 1, 2,
		closure setSlot(call evalArgAt(idx), call evalArgAt(idx + 1))
	)

	/* When a block is activated the following happens:
	 * - A clone of Lobby Locals is used as the activation record to hold local variables of the block.
	 *   The locals object has Locals as its proto. Locals do not have any protos (but contain a sometimes modified copy of Object's contents)
	 *   Additionally it respond to forward message (forward is called when a message cannot be looked up in the object) by forwarding the
	 *   message to self.
	 * - "self" is added to locals and it is set to the scope of the block (default scope is the receiver of the "block" message. 
	 *   If it is nil (default for methods), the current call target is used as the scope.
	 * - "call" is added to locals (http://iolanguage.org/scm/io/docs/reference/index.html#/Core/Core/Call)
	 * - Local variables are added to locals.
	 *
	 * Note that "block(...) setScope(nil) setIsActivatable(true)" should be the same as "method(...)".
	 */
	newBlk := block(
		closure := self shallowCopy setProto(call target)
		call delegateToMethod(closure, "__method")
	) setScope(closure) setIsActivatable(true)
)

x := 1
y := 2
f1 := method((x + y) println)
f2 := methodClosure("f1", "x", 3+y, "y", 8)
f1
f2


/* ASSIGNMENT:
 * Create function makeAdder that returns a function which adds a previously defined value to its parameter.
 */

// incr3 := makeAdder(3) 
// incr5 := makeAdder(5)

// incr3(7) println   // 10
// incr5(7) println   // 12
