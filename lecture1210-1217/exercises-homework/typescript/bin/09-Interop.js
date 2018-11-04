"use strict";
var e09;
(function (e09) {
    var http = require('http');
    var fs = require('fs');
    function staticHandler(request, response) {
        if (request.url == '/') {
            fs.readFile(__dirname + '/index.html', function (err, data) {
                response.writeHead(200, { 'Content-Type': 'text/html' });
                response.write(data);
                response.end();
            });
        }
        else if (request.url == '/09-Interop_Client.js') {
            fs.readFile(__dirname + '/09-Interop_Client.js', function (err, data) {
                response.writeHead(200, { 'Content-Type': 'text/html' });
                response.write(data);
                response.end();
            });
        }
    }
    var app = http.createServer(staticHandler);
    console.log("Listening on localhost:8080 ...");
    app.listen(8080);
})(e09 || (e09 = {}));
//# sourceMappingURL=09-Interop.js.map