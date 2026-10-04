import helpers.*

String sentimentOf(String text) {
    llm = new LLMChatConnector()
    llm.systemPrompt = '''
You are a sentiment classifier.
Classify the supplied text using exactly one of these labels:
positive
negative
neutral
Return only the label and nothing else.
'''.trim()

    String result = llm.chat(text).trim().toLowerCase()

    switch (result) {
        case 'positive':
        case 'negative':
        case 'neutral':
            return result

        default:
            throw new RuntimeException(
                "Unexpected sentiment returned by LLM: ${result}"
            )
    }
}

String.metaClass.sentimentOf = {->
    sentimentOf(delegate)
}

println "That is very kind of you!".sentimentOf()
println "I went home right after school.".sentimentOf()
println "I can't cope with her laziness.".sentimentOf()
println ''
println 'This restaurant was absolutely wonderful!'.sentimentOf()
println 'My trip to Berlin was ok.'.sentimentOf()
println 'She is such a ...'.sentimentOf()
