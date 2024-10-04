def global = {
final a = {
    final b = {
        final c = {magicNumber + 10}
        c()
    }
    b()
}

//TASK Set the delegates so that to code passes and uses the value below

println a()
assert 30 == a()
}
final data = [magicNumber: 20]
global.delegate = data
global()