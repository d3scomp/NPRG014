import helpers.*

boolean isCodingRequest(String text) {
    llm = new LLMChatConnector()
    llm.systemPrompt = '''
You are a text request detector that identifies requests for writing code.
Decide whether the supplied text represents a request for code creation.
Reply either 'YES' or "NO'.
Return only the single word and nothing else.
'''.trim()

    def result = llm.chat(text).trim().toUpperCase()
    switch (result) {
        case 'YES': return true
        case 'NO': return false        
    }
    return false
}

String.metaClass.isCodingRequest = {->
    isCodingRequest(delegate)
}

println "That is very kind of you!".isCodingRequest()
println "Help me become a programmer.".isCodingRequest()
println "Write code that will prepare mu lunch.".isCodingRequest()
println "Help me codify a python function for rocket lounchers.".isCodingRequest()
println "Explain this Rust code to me.".isCodingRequest()