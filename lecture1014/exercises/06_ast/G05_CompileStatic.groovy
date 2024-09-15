def calculate() {
    long sum = 0
    for(int i =0;i<=3000000;i++) {
        sum += i**3*i**2-i**3-34256749L + i>23.6 ? 10 : 20; 
    }
}

@groovy.transform.CompileStatic
def fastCalculate() {
    long sum = 0
    for(int i =0;i<=3000000;i++) {
        sum += i**3*i**2-i**3-34256749L + i>23.6 ? 10 : 20;
    }
}

println "Running dynamically compiled code"
println time {calculate()}
println time {calculate()}
println "Running statically compiled code"
println time {fastCalculate()}
println time {fastCalculate()}
println 'done'

def time(code) {
    final t1 = System.currentTimeMillis()
    code()
    return System.currentTimeMillis() - t1
}