class HtmlMethods {
    public static String html(StringBuilder self, Closure code) {
        code.delegate = self
        self.append("<html>\n")
        self.append(code.call())
        self.append("</html>\n")
        return self.toString()
    }
    public static String body(StringBuilder self, Closure code) {
        code.delegate = self    
        self.append("<body>\n")
        self.append(code.call())
        self.append("</body>\n")
        return ""        
    }
    public static String div(StringBuilder self, Closure code) {
        code.delegate = self    
        self.append("<div>\n")
        self.append(code.call())
        self.append("</div>\n")
        return ""
    }
    public static String p(StringBuilder self, Closure code) {
        code.delegate = self    
        self.append("<p>\n")
        self.append(code.call())
        self.append("</p>\n")
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