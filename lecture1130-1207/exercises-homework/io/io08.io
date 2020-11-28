DescriptionUtils decorateObject

Object myMethod := method(
	bodyMsg := call argAt(call argCount - 1)
	
	argNames := call message arguments map(x, x name)
	argNames removeLast
	
	mtd := Block clone
	mtd setArgumentNames(argNames)
	mtd setMessage(bodyMsg) setScope(nil) setIsActivatable(true)
)
		
Object constructorMethod := method(protObj,
	bodyMsg := call argAt(call argCount - 1)
	
	argNames := call message arguments rest map(x, x name)
	argNames removeLast
	
	mtd := block(
		this := protObj clone
		
		locals := Locals clone
		locals self := this
		
		argNames foreach(i, v, locals setSlot(v, call evalArgAt(i)))
		
		bodyMsg doInContext(locals)
		this
	) setIsActivatable(true)
)

Shape := Object clone
Shape prefix := "$ "

Shape hello := myMethod(name,
	"#{prefix}Hello #{name}!" interpolate println
)

newA := constructorMethod(Shape, prefix,
	tmp := prefix .. ">>> "
	self prefix := tmp
)

a := newA("maia")
a pd

a hello("World")



/* ASSIGNMENT:
 * Write a method "nonvirtualMethod", which returns a method that is
 * always exectuted in the context of the object on which "nonvirtualMethod" 
 * was called.
 */
 
//Shape hello2 := Shape nonvirtualMethod(name,
//	"#{prefix}Hello #{name}!" interpolate println
//)

//a hello2("World") // Should output "$ Hello World!"