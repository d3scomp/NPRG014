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
}

class None extends Maybe<Object> {
    public <B> Maybe<B> map(Closure<B> f) {
        return new None()
    }
}

def a = new Some<Integer>(10)
def b = a.map {Integer v -> "value: $v"}
println b
def c = a.map {Integer v -> if (true) throw new RuntimeException('test'); return ""}
println c
