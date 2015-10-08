def binding = new Binding()
binding['a'] = 10
binding['b'] = 20
GroovyShell shell = new GroovyShell(binding)

final code = '''
note = 'This is a boring calculation!'

println 'Calculating ...'
a + b + 9

'''

println 'Result: ' + shell.evaluate(code)

//TASK Read and print the 'note' bound by the script
