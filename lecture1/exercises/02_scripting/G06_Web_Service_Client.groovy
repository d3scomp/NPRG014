@Grab(group = 'org.codehaus.groovy.modules', module = 'groovyws', version = '0.5.2')
import groovyx.net.ws.WSClient

proxy = new WSClient("http://www.w3schools.com/webservices/tempconvert.asmx?WSDL", this.class.classLoader)
proxy.initialize()

result = proxy.CelsiusToFahrenheit(0)
println "You are probably freezing at ${result} degrees Farhenheit"

final temperatures = [-20, -10, 0, 10, 20, 30]
//TASK For each value in Celsius print out the value in Fahrenheit such as '0 Celsius is 32 Fahrenheit'
//TASK Do the web service access in parallel
//TASK Compare the time required for sequential and parallel version
