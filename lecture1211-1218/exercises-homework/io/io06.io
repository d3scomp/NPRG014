block(
	a := 1
	b := 2
	block(
		a := 3 // creates local a
		b = 4  // sets outer b
		"inner - a: #{a}, b: #{b}" interpolate println
	) call

	"outer - a: #{a}, b: #{b}" interpolate println
) call	
