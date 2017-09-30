//TASK Build a processor for user scripts that will be provided in plain text at runtime.
//On the input the processor receives a list of urls and a user script that performs user-defined filtering of these urls.
//The processor should return urls of the websites that were returned by the user script.
//The user scripts must be allowed to use custom commands - 'download', 'unless', talksAboutGroovy', 'remember' and 'remembered'.
//The semantics of these commands can be deduced from the 'test user script input', defined at the bottom of this assignment.
//Use the tricks we learnt about scripting, passing parameters to GroovyShell through binding, properties, 
//closure delegates, the 'object.with(Closure)' method, etc.


List<String> filterSitesByUserScript(String userScript, List<String> sites) {
    //Filtering function. Needs to be implemented

    return []
}





//************* Do not modify after this point!

//A test user script input.
String userInput = '''
    for(site in allSites) {
        def content = download site
        unless (talksAboutGroovy(content)) {
            remember site
        }
    }
    return remembered
'''

//Calling the filtering method on a list of sites. 
sites = ["http://groovy.cz", "http://gpars.org", "http://groovy-lang.org/", "http://infoq.com", "http://oracle.com", "http://ibm.com"]
def result = filterSitesByUserScript(userInput, sites)
result.each {
    println 'No groovy mention at ' + it
}
assert result.size()>0 && result.size() < sites.size   
println 'ok'