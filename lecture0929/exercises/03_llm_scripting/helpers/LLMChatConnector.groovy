package helpers

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

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

    LLMChatConnector() {
    }

    LLMChatConnector(String baseUrl, String model) {
        this.baseUrl = baseUrl
        this.model = model
    }

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
     * Alias for {@link #appendMessage(String, String)}.
     */
    LLMChatConnector addMessage(String role, String content) {
        appendMessage(role, content)
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
     * Alias for {@link #appendUserMessage(String)}.
     */
    LLMChatConnector addUserMessage(String content) {
        appendUserMessage(content)
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
     * Alias for {@link #appendAssistantMessage(String)}.
     */
    LLMChatConnector addAssistantMessage(String content) {
        appendAssistantMessage(content)
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
     * Alias for {@link #appendSystemMessage(String)}.
     */
    LLMChatConnector addSystemMessage(String content) {
        appendSystemMessage(content)
    }

    /**
     * Clear all message history.
     */
    void clearMessages() {
        messages.clear()
    }

    /**
     * Alias for {@link #clearMessages()}.
     */
    void clearHistory() {
        clearMessages()
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

    /**
     * Alias for {@link #chat(String)}.
     */
    String send(String userMessage) {
        chat(userMessage)
    }

    /**
     * Alias for {@link #chat(String)}.
     */
    String ask(String userMessage) {
        chat(userMessage)
    }
}
