class A {
    String name = "A"
    
    def foo(f) {
        f.delegate = this
//        f.resolveStrategy = Closure...
        f()
    }
}

class B {
    String name = "B"
    def foo() {
        new A().foo {-> println this.name + ":" + owner.name + ":" + delegate.name + ":" + name}
    }
}

new B().foo()

//TASK Experiment with different resolution strategies