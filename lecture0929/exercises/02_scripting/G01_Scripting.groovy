def binding = new Binding()
binding['a'] = 10
binding['b'] = 20
GroovyShell shell = new GroovyShell(binding)

final code = '''
debugNote = 'This is a boring calculation!'

println 'Calculating ...'
a + b + 9

'''

println 'Result: ' + shell.evaluate(code)
println 'Debug note: ' + binding.debugNote

//TASK Read and print the value of 'a' after the computation
//println 'Value of a: '...