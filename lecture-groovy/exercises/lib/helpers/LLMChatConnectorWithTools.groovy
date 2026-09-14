package helpers

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Helper class that encapsulates the configuration and logic for connecting
 * to a local LLM chat completion API (e.g., Ollama /api/chat).
 * Maintains a conversation history of system, user, and assistant messages.
 * Natively supports Tool Calling (MCP) via an internal Agent Loop.
 */
class LLMChatConnectorWithTools {

    String baseUrl = 'http://localhost:11434/api/chat'
    String model = 'gemma3:4b'
    boolean stream = false
    String contentType = 'application/json'
    boolean debug = false

    String systemPrompt = 'You are a helpful assistant.'

    // Stores the conversation history. Changed to generic Map to support tool_calls payload
    List<Map> messages = []
    
    // Stores definitions and execution closures for registered tools
    List<Map> registeredTools = []

    LLMChatConnectorWithTools() {
    }

    LLMChatConnectorWithTools(String baseUrl, String model) {
        this.baseUrl = baseUrl
        this.model = model
    }

    /**
     * Registers a tool that the LLM can call during the chat loop.
     *
     * @param name The function name (e.g., "fetchWebPageContent")
     * @param description Explanation of what the tool does and when to use it
     * @param parameters JSON Schema map defining the arguments (type: object, properties, etc.)
     * @param action The Groovy Closure to execute when the tool is called. Receives an argument map.
     */
    void registerTool(String name, String description, Map parameters, Closure action) {
        registeredTools << [
            name: name,
            description: description,
            parameters: parameters,
            action: action
        ]
    }

    /**
     * Append a raw message map (useful for tool_calls and tool results).
     */
    LLMChatConnectorWithTools appendMessage(Map messageMap) {
        messages << messageMap
        this
    }

    /**
     * Append a message with a specific role and content to the history.
     *
     * @param role the message role (e.g., "user", "assistant", "system", "tool")
     * @param content the message content
     * @return this connector instance for method chaining
     */
    LLMChatConnectorWithTools appendMessage(String role, String content) {
        appendMessage([role: role, content: content])
    }

    LLMChatConnectorWithTools addMessage(String role, String content) {
        appendMessage(role, content)
    }

    LLMChatConnectorWithTools appendUserMessage(String content) {
        appendMessage('user', content)
    }

    LLMChatConnectorWithTools addUserMessage(String content) {
        appendUserMessage(content)
    }

    LLMChatConnectorWithTools appendAssistantMessage(String content) {
        appendMessage('assistant', content)
    }

    LLMChatConnectorWithTools addAssistantMessage(String content) {
        appendAssistantMessage(content)
    }

    LLMChatConnectorWithTools appendSystemMessage(String content) {
        appendMessage('system', content)
    }

    LLMChatConnectorWithTools addSystemMessage(String content) {
        appendSystemMessage(content)
    }

    /**
     * Appends a tool execution result to the history.
     */
    LLMChatConnectorWithTools appendToolMessage(String toolName, String content) {
        appendMessage([role: 'tool', name: toolName, content: content])
    }

    void clearMessages() {
        messages.clear()
    }

    void clearHistory() {
        clearMessages()
    }

    String chat(String userMessage) {
        appendUserMessage(userMessage)
        chat()
    }

    /**
     * Sends the current conversation history to the LLM endpoint.
     * Automatically loops to handle execution of any registered tools
     * until the LLM returns a final text response.
     *
     * @return the final assistant's response string
     */
    String chat() {
        int maxIterations = 10
        int loopCount = 0

        while (loopCount < maxIterations) {
            loopCount++
            Map responseMsg = executeLLMRequest()

            if (!responseMsg) {
                throw new RuntimeException("Received empty response from LLM API")
            }

            // 1. Append the assistant's exact response (preserves context if it includes tool_calls)
            appendMessage(responseMsg)

            // 2. Check if the LLM requested any tools
            if (responseMsg.tool_calls) {
                if (debug) println "=== Tool Calls Detected ==="

                responseMsg.tool_calls.each { tc ->
                    def func = tc.function
                    String toolName = func?.name
                    Map toolArgs = (func?.arguments instanceof Map) ? func.arguments : [:]

                    if (debug) println "Executing Tool: ${toolName} | Args: ${toolArgs}"

                    def tool = registeredTools.find { it.name == toolName }
                    String toolResultText

                    if (tool) {
                        try {
                            // Invoke the registered closure with the arguments map
                            Object result = tool.action.call(toolArgs)
                            toolResultText = result?.toString() ?: "Success"
                        } catch (Exception e) {
                            toolResultText = "Error executing tool '${toolName}': ${e.message}"
                        }
                    } else {
                        toolResultText = "Error: Tool '${toolName}' is not registered."
                    }

                    if (debug) println "Tool Result: ${toolResultText}"

                    // 3. Append the tool result so the LLM can read it on the next loop iteration
                    appendToolMessage(toolName, toolResultText)
                }
                
                // The loop continues, sending the tool results back to the LLM to get a final answer
            } else {
                // No tools requested, return the final text
                return responseMsg.content?.toString() ?: ""
            }
        }

        return "ERROR: Agent loop reached maximum tool call iterations (${maxIterations})."
    }

    /**
     * Core HTTP request handler. Extracts the payload building and response parsing.
     */
    private Map executeLLMRequest() {
        // Prepend system prompt as the first message if not already present
        if (systemPrompt && !messages.find { it.role == 'system' }) {
            messages.add(0, [role: 'system', content: systemPrompt])
        }

        def requestBody = [
            model   : model,
            messages: messages,
            stream  : stream
        ]

        // Only inject tools payload if tools have been registered
        if (registeredTools) {
            requestBody.tools = registeredTools.collect { tool ->
                [
                    type: "function",
                    function: [
                        name: tool.name,
                        description: tool.description,
                        parameters: tool.parameters
                    ]
                ]
            }
        }

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
        // Added timeouts so execution doesn't hang indefinitely 
        conn.connectTimeout = 120000
        conn.readTimeout = 120000

        conn.outputStream.withWriter('UTF-8') { writer ->
            writer.write(jsonBody)
        }

        // Improved error handling to expose the actual 500 error cause from Ollama logs
        if (conn.responseCode >= 400) {
            String errorMsg = conn.errorStream?.getText('UTF-8') ?: "Unknown HTTP Error"
            conn.disconnect()
            throw new RuntimeException("LLM API HTTP ${conn.responseCode}: ${errorMsg}")
        }

        def responseText = conn.inputStream.getText('UTF-8')
        conn.disconnect()

        if (debug) {
            println '=== LLM Response ==='
            println responseText
            println '===================='
        }

        def parsed = new JsonSlurper().parseText(responseText)
        
        // Return the raw message map (which natively holds role, content, and tool_calls)
        return parsed?.message as Map
    }

    String send(String userMessage) {
        chat(userMessage)
    }

    String ask(String userMessage) {
        chat(userMessage)
    }
}