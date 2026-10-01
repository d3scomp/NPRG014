import helpers.*

/**
 * Sentiment classification using a generative LLM.
 */
class SentimentConnector {

    LLMChatConnector llm

    SentimentConnector() {
        llm = new LLMChatConnector()
        llm.systemPrompt = '''
You are a sentiment classifier.

Classify the supplied text using exactly one of these labels:

positive
negative
neutral

Return only the label and nothing else.
'''.trim()
    }

    SentimentConnector(LLMChatConnector llm) {
        this.llm = llm
    }

    String hasSentimentOf(String text) {
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
}

def sentiment = new SentimentConnector()

println sentiment.hasSentimentOf(
    'This restaurant was absolutely wonderful!'
)
println sentiment.hasSentimentOf(
    'My trip to Berlin was ok.'
)
println sentiment.hasSentimentOf(
    'She is such a ...'
)