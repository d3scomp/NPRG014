class HtmlMethods {
    private static int indent = 0
    private static String indent() {
        ' '*(indent*4)
    }
    
    private static process(StringBuilder self, Closure code, String tag) {
        code.delegate = self
        self.append("\n${indent()}<$tag>\n")
        indent++
        self.append(indent())
        self.append(code.call())
        indent--                
        self.append("\n${indent()}</$tag>\n")
        return self.toString()
    }
    
    public static String html(StringBuilder self, Closure code) {
        process(self, code, 'html')
    }
    public static String body(StringBuilder self, Closure code) {
        process(self, code, 'body')    
        return ""
    }
    public static String div(StringBuilder self, Closure code) {
        process(self, code, 'div')
        return ""
    }
    public static String p(StringBuilder self, Closure code) {
        code.delegate = self    
        self.append("\n${indent()}<p>\n")
        indent++        
        self.append(indent())        
        self.append(code.call())
        indent--        
        self.append("\n${indent()}</p>\n")
        return ""
    }
}

use(HtmlMethods) {

    def doc = new StringBuilder().html {
        body {
            div {
                "content"
            }
            div {
                "some more content"
            }
            p {"This is a paragraph"}
        }
    }
println doc
}