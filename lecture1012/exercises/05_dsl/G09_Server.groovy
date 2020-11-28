//final out = new StringWriter()

def builder = new groovy.xml.MarkupBuilder(new OutputStreamWriter(System.out))  // Construct a builder

// Negotiate the protocol

println "HTTP/1.0 200 OK"
println "Content-Type: text/html"
println ""

// Create a simple html markup

final doc = builder.html {
    head {
        title 'Demo'
    }
    body {
        h1 'Hello'
        p {
            div 'How are you doing today, sir?'
        }
        p 'This is a cool Groovy script'
    }
}

//println out

"success"

//TASK Add a link to the page pointing to "https://groovy-lang.org/" and reload so as you can see it in the browser