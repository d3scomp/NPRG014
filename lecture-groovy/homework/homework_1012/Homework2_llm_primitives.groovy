import groovy.json.JsonOutput
import groovy.json.JsonSlurper


/**
 * HOMEWORK ASSIGNMENT: LLM-Powered Text Relationship Classifier
 * 2026/2027
 *
 * Your task is to implement the `TextClassificationCategory` class so that the 
 * test block bellow executes successfully.
 *
 * Using Groovy's Category pattern, you will extend the `String` class with two 
 * new methods that use an LLM to determine the logical relationship between two texts.
 *
 * REQUIREMENTS:
 * 1. Implement two methods: `supports` and `contradicts`. 
 * 2. Use the `LLMChatConnector` (as seen in lectures) to classify the relationship 
 *    between the two texts. 
 * 3. Write a precise `systemPrompt` that instructs the LLM to act as a classifier 
 *    and return EXACTLY one of three labels: 'SUPPORTS', 'CONTRADICTS', or 'NEUTRAL'.
 *
 * ERROR HANDLING:
 * - Input Validation: If either of the provided strings is null or empty, 
 *   throw an `IllegalArgumentException`.
 * - Output Validation: If the LLM returns anything other than the three expected 
 *   labels (even after trimming and case-correction), throw a `RuntimeException`.
 *
 * HINT: 
 * You might want to create a private helper method (e.g., `classify(String t1, String t2)`) 
 * to handle the LLM interaction and avoid duplicating code in your `supports` 
 * and `contradicts` methods.
 *
 * The LLMChatConnector class defined at the end of this script is a helper class that allows your code to communicate
 * with the LLM installed in Ollama by calling LLMChatConnector::chat.
 */
class TextClassificationCategory {
    // TODO: Implement your category methods here
}

use (TextClassificationCategory) {
    if ("There is no snow or rain fall forecast for the next couple of days.".supports("Tomorrow will be a day suitable for hiking in the mountains.")) {
        println "Supported"
    } else {
        assert false : 'Should have supported'
    }

    if ("The weather forcast for tommorow indicates modest temperatures, partly cloudy and no thunderstorms, not event in the mountains.".supports("Tomorrow will be a day suitable for hiking in the mountains.")) {
        println "Supported"
    } else {
        assert false : 'Should have supported'
    }

    if ("The experiment produced no statistically significant result.".contradicts("The experiment produced a significant result.")) {
        println "Contradicted"
    } else {
        assert false : 'Should have contradicted'
    }
}



/**
 * Helper class that encapsulates the configuration and logic for connecting
 * to a local LLM chat completion API (e.g., Ollama /api/chat).
 * Maintains a conversation history of system, user, and assistant messages.
 */
class LLMChatConnector {

    String baseUrl = 'http://localhost:11434/api/chat'
    String model = 'gemma3:4b'
    boolean stream = false
    String contentType = 'application/json'
    boolean debug = false

    String systemPrompt = 'You are a helpful assistant.'

    List<Map<String, String>> messages = []

    /**
     * Append a message with a specific role and content to the history.
     *
     * @param role the message role (e.g., "user", "assistant", "system")
     * @param content the message content
     * @return this connector instance for method chaining
     */
    LLMChatConnector appendMessage(String role, String content) {
        messages << [role: role, content: content]
        this
    }

    /**
     * Append a user message to the conversation history.
     *
     * @param content the user message content
     * @return this connector instance for method chaining
     */
    LLMChatConnector appendUserMessage(String content) {
        appendMessage('user', content)
    }

    /**
     * Append an assistant message to the conversation history.
     *
     * @param content the assistant message content
     * @return this connector instance for method chaining
     */
    LLMChatConnector appendAssistantMessage(String content) {
        appendMessage('assistant', content)
    }

    /**
     * Append a system message to the conversation history.
     *
     * @param content the system message content
     * @return this connector instance for method chaining
     */
    LLMChatConnector appendSystemMessage(String content) {
        appendMessage('system', content)
    }

    /**
     * Automatically appends the user message to history, sends the chat conversation
     * to the LLM endpoint, records the assistant's response in history, and returns it.
     *
     * @param userMessage the user message to append and send
     * @return the assistant's response string
     */
    String chat(String userMessage) {
        appendUserMessage(userMessage)
        chat()
    }

    /**
     * Sends the current conversation history to the LLM endpoint, records the assistant's
     * response in history, and returns it.
     *
     * @return the assistant's response string
     */
    String chat() {
        // Prepend system prompt as the first message if not already present
        if (systemPrompt && !messages.find { it.role == 'system' }) {
            messages.add(0, [role: 'system', content: systemPrompt])
        }
        def requestBody = [
            model   : model,
            messages: messages,
            stream  : stream
        ]
        def jsonBody = JsonOutput.toJson(requestBody)

        if (debug) {
            println '=== LLM Request ==='
            println "URL: ${baseUrl}"
            println "Body: ${jsonBody}"
            println '==================='
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(baseUrl).openConnection()
        conn.requestMethod = 'POST'
        conn.doOutput = true
        conn.setRequestProperty('Content-Type', contentType)

        conn.outputStream.withWriter('UTF-8') { writer ->
            writer.write(jsonBody)
        }

        def responseText = conn.inputStream.getText('UTF-8')
        conn.disconnect()

        if (debug) {
            println '=== LLM Response ==='
            println responseText
            println '===================='
        }

        def parsed = new JsonSlurper().parseText(responseText)
        def assistantContent = parsed?.message?.content?.toString() ?: ''

        appendAssistantMessage(assistantContent)
        assistantContent
    }
}