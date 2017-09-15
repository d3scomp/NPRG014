/*
Features:
- interoperability with existing JS code

Type packages can be looked up here: https://microsoft.github.io/TypeSearch/ and installed via npm (see package.json)
Another repo with TypeScrip Type definitions: http://definitelytyped.org/
*/

import {IncomingMessage} from "http"
import {ServerResponse} from "http"

namespace e09 {
    let http = require('http')
    let fs = require('fs')

    function staticHandler(request: IncomingMessage, response: ServerResponse) {
        if (request.url == '/') {
            fs.readFile(__dirname + '/index.html', (err, data) => {
                response.writeHead(200, {'Content-Type': 'text/html'})
                response.write(data)
                response.end()
            })
        } else if (request.url == '/09-Interop_Client.js') {
            fs.readFile(__dirname + '/09-Interop_Client.js', (err, data) => {
                response.writeHead(200, {'Content-Type': 'text/html'})
                response.write(data)
                response.end()
            })
        }
    }

    var app = http.createServer(staticHandler)

    console.log("Listening on localhost:8080 ...")
    app.listen(8080)
}