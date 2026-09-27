GroovyShell shell = new GroovyShell()

final code = '''
debugNote = 'This is a boring calculation!'

println 'Calculating ...'
a + b + 9

'''

final script = shell.parse(code)
script.a = 10
script.setProperty("b", 20)

println 'Result: ' + script.run()
println 'Debug note: ' + script.debugNote
