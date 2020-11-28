testMethod := method(x, y,
	thisLocalContext slotNames join(", ") println
)

testMethod(1, 2)


/* ASSIGNMENT
 * Write method debugPrintln, which for the statements below
 * prints out the given output (note that the output contains
 * name of the variable).
 */

x := 3
y := 4
// x debugPrintln    // "x = 3"
// y debugPrintln    // "y = 4"

