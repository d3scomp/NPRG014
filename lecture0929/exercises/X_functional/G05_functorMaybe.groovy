abstract class Maybe<A> {
    public abstract <B> Maybe<B> map(Closure<B> f);
    
    public A getValue() {
        return null;
    }
}

class Some<A> extends Maybe<A> {
    private final A value
    
    public Some(final A value) {
        this.value = value
    }

    @Override    
    public A getValue() {
        return value;
    }
    
    public <B> Maybe<B> map(Closure<B> f) {
        try {
            B result = f(this.value)
            return new Some(result)
        } catch (all) {
            return new None<B>()
        }
    } 
    
    public String toString() {"Some with value " + value}   
}

class None<A> extends Maybe<A> {
    public <B> Maybe<B> map(Closure<B> f) {
        return new None<B>()
    }
    
    public String toString() {"None"}
}

def a = new Some<Integer>(10)
def b = a.map {Integer v -> "====>: $v"}
println b
def c = a.map {Integer v -> if (true) throw new RuntimeException('test'); return ""}
println 'After an exception was thrown: ' + c


// API may use Maybe as a safe way to report errors in computation
Maybe<Integer> divide(Integer a, Integer b) {
    if (b!=0) return new Some(a/b)
    else return new None()
}

println divide(10, 5).value
println divide(10, 5).map {it+1}.map {-1*it}
println divide(0, 5).map {it+1}.map {-1*it}
println divide(10, 0).map {it+1}.map {-1*it}
println divide(500, 5).map {divide(it, 0)}.map {it+1}.map {-1*it}