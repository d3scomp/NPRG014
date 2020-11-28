myif := method(condition,
	index := if(condition, 1, 2)
	call evalArgAt(index)
)

myif(19 > 5,
	writeln("19 > 5. Correct.")
,
	writeln("19 > 5. Incorrect.")
)

/* ASSIGNMENT:
 * Create function myuntil, which takes parameters <body>, <condition>. The function
 * executes the body as long as the condition is false. The evaluation happens
 * within the context in which myuntil has been called.
 */

// This should count to three		
// b := 1 
// myuntil(
// 	b println
//	b = b + 1,
//	b > 3
//)
