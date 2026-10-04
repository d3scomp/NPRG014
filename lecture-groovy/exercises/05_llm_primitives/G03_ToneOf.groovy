import helpers.*

// TASK Implement the toneOf method that instructs LLM to classify the tone of voice used in the supplied text, attach the method to the metaClass of String.
String toneOf(String text) {
    llm = new LLMChatConnector()

    return llm.chat(text).trim().toLowerCase()
}


assert 'friendly' == "That is very kind of you!".toneOf()
assert 'angry' == "Go away!.".toneOf()
assert 'anxious' == "I'm afraid this test will not go well.".toneOf()

println 'ok'