import helpers.*

String toneOf(String text) {
    llm = new LLMChatConnector()
    llm.systemPrompt = '''
You are a text tone classifier.
Classify the supplied text using exactly one of these labels:
friendly
anxious
angry
neutral
Return only the label and nothing else.
'''.trim()

    return llm.chat(text).trim().toLowerCase()
}

String.metaClass.toneOf = {->
    toneOf(delegate)
}

assert 'friendly' == "That is very kind of you!".toneOf()
assert 'angry' == "Go away!.".toneOf()
assert 'anxious' == "I'm afraid this test will not go well.".toneOf()

println 'ok'