final a = {
    final b = {
        final c = {magicNumber + 10}
        c()
    }
    b()
}

//TASK Set the delegates so that to code passes and uses the value below
final data = [magicNumber: 20]
println a()