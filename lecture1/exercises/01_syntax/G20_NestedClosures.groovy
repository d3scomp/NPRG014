final a = {
    final b = {
        final c = {value + 10}
        c()
    }
    b()
}

//TASK Set the delegates so that to code passes and uses the value below
final values = [value: 20]

println a()