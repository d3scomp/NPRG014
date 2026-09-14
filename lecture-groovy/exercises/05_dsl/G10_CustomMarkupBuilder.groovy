class MyBuilder {

    def invokeMethod(String methodName, args) {
        def result = '';
        if (args.size() > 0) {
            Closure closure = args[0]
            closure.delegate = this
            result = closure()
        }
        return "<$methodName>$result</$methodName>"
    }
}



def doc = new MyBuilder().html {
    body {
        div {
            "content"
        }
    }
}

println doc