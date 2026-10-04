class MyIndentingBuilder {

    def indent = 0
    def indentSize = 1

    def invokeMethod(String methodName, args) {
        def result = '';
        if (args.size() > 0) {
            Closure closure = args[0]
            closure.delegate = this
            result = closure()
        }
        return "<$methodName>\n${' ' * (indent+indentSize)}$result\n${' ' * (indent)}</$methodName>"
    }
}

//TASK manipulate the value in "indent" so as the generated xml is nicely indented

def doc = new MyIndentingBuilder().html {
    body {
        div {
            "content"
        }
    }
}

println doc