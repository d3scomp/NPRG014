package helpers

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Helper class for connecting to a remote OpenAI-compatible
 * LLM Chat Completions API (e.g. OpenAI GPT).
 *
 * Maintains a conversation history of system, user, and assistant messages.
 *
 * The API key can be supplied through the apiKey property or, if absent,
 * loaded from the OPENAI_API_KEY system environment variable.
 */
class LLMChatConnectorOpenAI {

    /**
     * OpenAI-compatible Chat Completions endpoint.
     *
     * For OpenAI this is:
     * https://api.openai.com/v1/chat/completions
     */
    String baseUrl = 'https://api.openai.com/v1/chat/completions'

    /**
     * Model to use.
     * Change this to any model available through the selected API.
     */
    String model = 'gpt-5-mini'

    /**
     * API key. If null/empty, OPENAI_API_KEY is used.
     */
    String apiKey

    boolean stream = false
    String contentType = 'application/json'
    boolean debug = false

    String systemPrompt = 'You are a helpful assistant.'

    List<Map<String, String>> messages = []

    LLMChatConnectorOpenAI() {
    }

    LLMChatConnectorOpenAI(String baseUrl, String model) {
        this.baseUrl = baseUrl
        this.model = model
    }

    LLMChatConnectorOpenAI(String baseUrl, String model, String apiKey) {
        this.baseUrl = baseUrl
        this.model = model
        this.apiKey = apiKey
    }

    /**
     * Returns the configured API key.
     *
     * If no key was explicitly configured, OPENAI_API_KEY
     * is read from the system environment.
     */
    protected String getEffectiveApiKey() {
        def key = apiKey?.trim()

        if (!key) {
            key = System.getenv('OPENAI_API_KEY')?.trim()
        }

        if (!key) {
            throw new IllegalStateException(
                    'No API key configured. Set the apiKey property or the OPENAI_API_KEY environment variable.'
            )
        }

        key
    }

    /**
     * Append a message with a specific role and content.
     */
    LLMChatConnectorOpenAI appendMessage(String role, String content) {
        messages << [role: role, content: content]
        this
    }

    /**
     * Alias for appendMessage().
     */
    LLMChatConnectorOpenAI addMessage(String role, String content) {
        appendMessage(role, content)
    }

    /**
     * Append a user message.
     */
    LLMChatConnectorOpenAI appendUserMessage(String content) {
        appendMessage('user', content)
    }

    /**
     * Alias for appendUserMessage().
     */
    LLMChatConnectorOpenAI addUserMessage(String content) {
        appendUserMessage(content)
    }

    /**
     * Append an assistant message.
     */
    LLMChatConnectorOpenAI appendAssistantMessage(String content) {
        appendMessage('assistant', content)
    }

    /**
     * Alias for appendAssistantMessage().
     */
    LLMChatConnectorOpenAI addAssistantMessage(String content) {
        appendAssistantMessage(content)
    }

    /**
     * Append a system message.
     */
    LLMChatConnectorOpenAI appendSystemMessage(String content) {
        appendMessage('system', content)
    }

    /**
     * Alias for appendSystemMessage().
     */
    LLMChatConnectorOpenAI addSystemMessage(String content) {
        appendSystemMessage(content)
    }

    /**
     * Clear all message history.
     */
    void clearMessages() {
        messages.clear()
    }

    /**
     * Alias for clearMessages().
     */
    void clearHistory() {
        clearMessages()
    }

    /**
     * Append a user message, send the conversation,
     * record the assistant response, and return it.
     */
    String chat(String userMessage) {
        appendUserMessage(userMessage)
        chat()
    }

    /**
     * Send the current conversation to the OpenAI-compatible
     * Chat Completions endpoint.
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
        def effectiveApiKey = getEffectiveApiKey()

        if (debug) {
            println '=== LLM Request ==='
            println "URL: ${baseUrl}"
            println "Model: ${model}"
            println "Body: ${jsonBody}"
            println '==================='
        }

        HttpURLConnection conn =
                (HttpURLConnection) new URL(baseUrl).openConnection()

        conn.requestMethod = 'POST'
        conn.doOutput = true
        conn.connectTimeout = 30000
        conn.readTimeout = 120000

        conn.setRequestProperty(
                'Content-Type',
                contentType
        )

        conn.setRequestProperty(
                'Authorization',
                "Bearer ${effectiveApiKey}"
        )

        conn.outputStream.withWriter('UTF-8') { writer ->
            writer.write(jsonBody)
        }

        int statusCode = conn.responseCode

        /*
         * OpenAI-compatible APIs normally return errors in the
         * error stream rather than the input stream.
         */
        def responseText

        if (statusCode >= 200 && statusCode < 300) {
            responseText = conn.inputStream.getText('UTF-8')
        } else {
            responseText = conn.errorStream?.getText('UTF-8') ?: ''
            conn.disconnect()

            throw new RuntimeException(
                    "LLM API request failed with HTTP ${statusCode}: ${responseText}"
            )
        }

        conn.disconnect()

        if (debug) {
            println '=== LLM Response ==='
            println responseText
            println '===================='
        }

        def parsed = new JsonSlurper().parseText(responseText)

        /*
         * OpenAI Chat Completions response format:
         *
         * {
         *   "choices": [
         *     {
         *       "message": {
         *         "role": "assistant",
         *         "content": "..."
         *       }
         *     }
         *   ]
         * }
         */
        def assistantContent =
                parsed?.choices?.getAt(0)?.message?.content?.toString() ?: ''

        appendAssistantMessage(assistantContent)

        assistantContent
    }

    /**
     * Alias for chat(String).
     */
    String send(String userMessage) {
        chat(userMessage)
    }

    /**
     * Alias for chat(String).
     */
    String ask(String userMessage) {
        chat(userMessage)
    }

    /**
     * Alias for chat(String).
     */
    String generate(String userMessage) {
        chat(userMessage)
    }
}
