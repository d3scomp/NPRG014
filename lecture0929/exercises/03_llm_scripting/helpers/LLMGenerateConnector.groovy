package helpers

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Helper class that encapsulates the configuration and logic for connecting
 * to a local LLM generation API (e.g., Ollama /api/generate) and generating
 * a response from a single prompt.
 */
class LLMGenerateConnector {

    String baseUrl = 'http://localhost:11434/api/generate'
    String model = 'gemma3:4b'
    boolean stream = false
    String contentType = 'application/json'
    boolean debug = false

    LLMGenerateConnector() {
    }

    LLMGenerateConnector(String baseUrl, String model) {
        this.baseUrl = baseUrl
        this.model = model
    }

    /**
     * Send a prompt to the LLM and return the raw response string.
     *
     * @param prompt the text prompt to send
     * @return the model's response as a plain string
     */
    String generate(String prompt) {
        def requestBody = [
            model  : model,
            prompt : prompt,
            stream : stream
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
        parsed?.response as String
    }

    /**
     * Alias for {@link #generate(String)}.
     */
    String send(String userMessage) {
        generate(userMessage)
    }

    /**
     * Alias for {@link #generate(String)}.
     */
    String ask(String userMessage) {
        generate(userMessage)
    }
}