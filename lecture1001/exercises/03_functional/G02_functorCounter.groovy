class Counter<A> {
    A value
    
    //f: A -> B
    def <B> Counter<B> map(Closure<B> f) {
        new Counter<B>(value: f(this.value))
    }
}
def increment = {it + 1}

def c = new Counter<Integer>(value: 0)
def r1 = c.map(increment)
println r1.value

def r2 = c.map(increment).map{2*it}
println r2.value

def r3 = c.map{"value: $it"}.map{it.size()}
println r3.value
