//TASK Build a processor for user scripts that will be provided in plain text at runtime. The processor should output urls of websites, returned by the user script.
//The user scripts may be allowed to use custom commands - 'download', 'unless', talksAboutGroovy', 'remember' and 'remembered'.
//Use the tricks we learnt about scripting, passing parameters to GroovyShell through binding, properties, 
//closure delegates, the 'object.with(Closure)' method, etc.


List<String> filterSitesByUserScript(String userScript, List<String> sites) {
    //Filtering function. Needs to be implemented

    return []
}





//************* Do not modify after this point!

//A test user input.
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
def result = filterSitesByUserScript(userInput, ["http://groovy.cz", "http://groovy-lang.org/", "http://infoq.com", "http://oracle.com", "http://ibm.com"])
result.each {
    println 'No groovy mention at ' + it
}
assert result.size()>0 && result.size() < 5   
println 'ok'