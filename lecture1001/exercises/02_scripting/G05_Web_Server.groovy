@Grab('org.mortbay.jetty:jetty:6.1.22')
import org.mortbay.jetty.*;
import org.mortbay.jetty.handler.*

//TASK Run and connect to the server from your browser

def server = new Server(8086)
def resourceHandler = new ResourceHandler(welcomeFiles: ["welcome.txt"], resourceBase: "exercises")
server.handler = new HandlerList(handlers: [resourceHandler, new DefaultHandler()])

server.start()
server.join()