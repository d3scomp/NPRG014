import groovy.xml.MarkupBuilder

def server = new ServerSocket(8080)  // listen on port 8080
println "Server started on port 8080"

while (true) {
    server.accept { socket ->
        println 'Accepted a connection'
        socket.withStreams { input, output ->
            println 'Using the socket'
            def reader = input.newReader()
            // Read request headers until an empty line
            while (true) {
                def line = reader.readLine()
                if (!line || line.trim().isEmpty()) break
            }        
            def writer = new OutputStreamWriter(output)
            def builder = new MarkupBuilder(writer)

            writer << "HTTP/1.0 200 OK\r\n"
            writer << "Content-Type: text/html\r\n"
            writer << "\r\n"

            builder.html {
                head {
                    title 'Demo'
                }
                body {
                    h1 'Hello'
                    p {
                        div 'How are you doing today, sir?'
                    }
                    p 'This is a cool Groovy script'
                    a(href: "https://groovy-lang.org/", "Groovy website")
                }
            }

            writer.flush()
            writer.close()
            input.close()
        }
        socket.close()
        println 'Closed the socket'
    }
}