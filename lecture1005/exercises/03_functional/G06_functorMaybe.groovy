abstract class Maybe<A> {
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
}

class None<A> extends Maybe<A> {
    public <B> Maybe<B> map(Closure<B> f) {
        return new None()
    }
}

class MaybeUtil<A, B> {
    //Maybe<A>, f: A -> B -> Maybe<B>
    public static final Closure<Maybe<B>> map = {Maybe<A> a, Closure<B> f ->
        if (a instanceof None) return new None<B>()
        try {
            B result = f(a.value)
            return new Some(result)
        } catch (all) {
            return new None()
        }
    }    
}

import static MaybeUtil.*

def a = new Some<Integer>(10)
def b = map(a) {Integer v -> "value: $v"}
println b
def c = map(a) {Integer v -> if (true) throw new RuntimeException('test'); return ""}
println c

//Maybe<Integer> -> Maybe<String>
def reporter = map.rcurry {Integer v -> "value: $v"}
println reporter(a)