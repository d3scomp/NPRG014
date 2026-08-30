package helpers

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Helper class for connecting to an OpenAI-compatible
 * Chat Completions API.
 *
 * API key:
 *   1. apiKey property, if supplied
 *   2. OPENAI_API_KEY environment variable otherwise
 *
 * The connector exposes the main Chat Completions parameters.
 * Optional parameters are only sent when they are non-null.
 *
 * Note:
 * Not every model supports every parameter. In particular,
 * reasoning models may reject some sampling parameters such
 * as temperature, top_p, etc.
 */
class LLMChatConnectorOpenAIWithParams {

    // ---------------------------------------------------------
    // Connection
    // ---------------------------------------------------------

    String baseUrl = 'https://api.openai.com/v1/chat/completions'
    String model = 'gpt-5-mini'
    String apiKey

    String contentType = 'application/json'

    boolean debug = false

    int connectTimeout = 30000
    int readTimeout = 120000


    // ---------------------------------------------------------
    // Generation parameters
    // ---------------------------------------------------------

    /**
     * Sampling temperature, normally 0.0 - 2.0.
     *
     * null = let the model/API use its default.
     */
    Double temperature

    /**
     * Nucleus sampling, normally 0.0 - 1.0.
     *
     * null = let the model/API use its default.
     *
     * OpenAI recommends changing either temperature OR topP,
     * rather than both.
     */
    Double topP

    /**
     * Maximum number of output tokens.
     *
     * For current OpenAI models, maxCompletionTokens is
     * preferred over the older maxTokens parameter.
     */
    Integer maxCompletionTokens

    /**
     * Number of completions to generate.
     *
     * Supported by Chat Completions, but not by every model/API.
     */
    Integer n

    /**
     * Sequences which cause generation to stop.
     */
    List<String> stop

    /**
     * Penalizes tokens according to their existing frequency.
     *
     * Range normally -2.0 to 2.0.
     */
    Double frequencyPenalty

    /**
     * Penalizes tokens that have already appeared.
     *
     * Range normally -2.0 to 2.0.
     */
    Double presencePenalty

    /**
     * Optional deterministic seed.
     *
     * Not guaranteed to produce identical output across
     * model/backend changes.
     */
    Integer seed


    // ---------------------------------------------------------
    // Log probabilities
    // ---------------------------------------------------------

    /**
     * Return log probabilities for generated tokens.
     */
    Boolean logprobs

    /**
     * Number of alternative token probabilities to return.
     *
     * Range: 0 - 20.
     */
    Integer topLogprobs


    // ---------------------------------------------------------
    // Reasoning / verbosity
    // ---------------------------------------------------------

    /**
     * Reasoning effort for models that support it.
     *
     * Examples include:
     *   none
     *   minimal
     *   low
     *   medium
     *   high
     *   xhigh
     *   max
     *
     * Exact values depend on the model.
     */
    String reasoningEffort

    /**
     * Output verbosity where supported.
     *
     * Typically:
     *   low
     *   medium
     *   high
     */
    String verbosity


    // ---------------------------------------------------------
    // Response format
    // ---------------------------------------------------------

    /**
     * Optional response format.
     *
     * Examples:
     *
     * [type: 'text']
     *
     * [type: 'json_object']
     *
     * For JSON Schema:
     *
     * [
     *   type: 'json_schema',
     *   json_schema: [
     *       name: 'MySchema',
     *       strict: true,
     *       schema: [...]
     *   ]
     * ]
     */
    Map responseFormat


    // ---------------------------------------------------------
    // Tools / function calling
    // ---------------------------------------------------------

    /**
     * Tools available to the model.
     *
     * Example:
     *
     * tools = [
     *     [
     *         type: 'function',
     *         function: [
     *             name: 'get_weather',
     *             description: 'Get current weather',
     *             parameters: [
     *                 type: 'object',
     *                 properties: [
     *                     city: [type: 'string']
     *                 ],
     *                 required: ['city']
     *             ]
     *         ]
     *     ]
     * ]
     */
    List<Map> tools

    /**
     * Controls tool selection.
     *
     * Examples:
     *   'none'
     *   'auto'
     *   'required'
     *
     * Or a map selecting a particular function.
     */
    Object toolChoice

    /**
     * Whether multiple tool calls may be executed in parallel.
     */
    Boolean parallelToolCalls


    // ---------------------------------------------------------
    // Request / service options
    // ---------------------------------------------------------

    /**
     * Whether OpenAI should store the completion.
     */
    Boolean store

    /**
     * Arbitrary metadata associated with the request.
     */
    Map<String, String> metadata

    /**
     * Service tier where supported.
     *
     * Examples:
     *   auto
     *   default
     *   flex
     *   scale
     *   priority
     */
    String serviceTier

    /**
     * Identifier for the end user.
     *
     * Deprecated by current OpenAI API in favor of
     * safetyIdentifier / promptCacheKey, but retained here
     * for compatibility with OpenAI-compatible servers.
     */
    String user

    /**
     * Safety identifier where supported.
     */
    String safetyIdentifier

    /**
     * Prompt cache key where supported.
     */
    String promptCacheKey

    /**
     * Prompt cache retention where supported.
     *
     * Example:
     *   in_memory
     *   24h
     */
    String promptCacheRetention


    // ---------------------------------------------------------
    // Streaming
    // ---------------------------------------------------------

    boolean stream = false

    /**
     * Streaming-specific options.
     *
     * Example:
     *
     * [includeUsage: true]
     */
    Map streamOptions


    // ---------------------------------------------------------
    // Conversation
    // ---------------------------------------------------------

    String systemPrompt = 'You are a helpful assistant.'

    List<Map<String, String>> messages = []


    // ---------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------

    LLMChatConnectorOpenAIWithParams() {
    }

    LLMChatConnectorOpenAIWithParams(String baseUrl, String model) {
        this.baseUrl = baseUrl
        this.model = model
    }

    LLMChatConnectorOpenAIWithParams(
            String baseUrl,
            String model,
            String apiKey
    ) {
        this.baseUrl = baseUrl
        this.model = model
        this.apiKey = apiKey
    }


    // ---------------------------------------------------------
    // API key
    // ---------------------------------------------------------

    /**
     * Get the effective API key.
     *
     * Explicit apiKey takes precedence over the environment.
     */
    protected String getEffectiveApiKey() {

        def key = apiKey?.trim()

        if (!key) {
            key = System.getenv('OPENAI_API_KEY')?.trim()
        }

        if (!key) {
            throw new IllegalStateException(
                    'No API key configured. ' +
                            'Set the apiKey property or the OPENAI_API_KEY environment variable.'
            )
        }

        return key
    }


    // ---------------------------------------------------------
    // Message handling
    // ---------------------------------------------------------

    LLMChatConnectorOpenAI appendMessage(
            String role,
            String content
    ) {
        messages << [
                role   : role,
                content: content
        ]

        this
    }

    LLMChatConnectorOpenAI addMessage(
            String role,
            String content
    ) {
        appendMessage(role, content)
    }

    LLMChatConnectorOpenAI appendUserMessage(
            String content
    ) {
        appendMessage('user', content)
    }

    LLMChatConnectorOpenAI addUserMessage(
            String content
    ) {
        appendUserMessage(content)
    }

    LLMChatConnectorOpenAI appendAssistantMessage(
            String content
    ) {
        appendMessage('assistant', content)
    }

    LLMChatConnectorOpenAI addAssistantMessage(
            String content
    ) {
        appendAssistantMessage(content)
    }

    LLMChatConnectorOpenAI appendSystemMessage(
            String content
    ) {
        appendMessage('system', content)
    }

    LLMChatConnectorOpenAI addSystemMessage(
            String content
    ) {
        appendSystemMessage(content)
    }

    void clearMessages() {
        messages.clear()
    }

    void clearHistory() {
        clearMessages()
    }


    // ---------------------------------------------------------
    // Request construction
    // ---------------------------------------------------------

    /**
     * Build the JSON request.
     *
     * Optional parameters are only added when non-null.
     */
    protected Map buildRequestBody() {

        def requestBody = [
                model   : model,
                messages: messages,
                stream  : stream
        ]

        // Generation
        if (temperature != null)
            requestBody.temperature = temperature

        if (topP != null)
            requestBody.top_p = topP

        if (maxCompletionTokens != null)
            requestBody.max_completion_tokens = maxCompletionTokens

        if (n != null)
            requestBody.n = n

        if (stop != null)
            requestBody.stop = stop

        if (frequencyPenalty != null)
            requestBody.frequency_penalty = frequencyPenalty

        if (presencePenalty != null)
            requestBody.presence_penalty = presencePenalty

        if (seed != null)
            requestBody.seed = seed

        // Log probabilities
        if (logprobs != null)
            requestBody.logprobs = logprobs

        if (topLogprobs != null)
            requestBody.top_logprobs = topLogprobs

        // Reasoning / output
        if (reasoningEffort != null)
            requestBody.reasoning_effort = reasoningEffort

        if (verbosity != null)
            requestBody.verbosity = verbosity

        // Response format
        if (responseFormat != null)
            requestBody.response_format = responseFormat

        // Tools
        if (tools != null)
            requestBody.tools = tools

        if (toolChoice != null)
            requestBody.tool_choice = toolChoice

        if (parallelToolCalls != null)
            requestBody.parallel_tool_calls = parallelToolCalls

        // Request options
        if (store != null)
            requestBody.store = store

        if (metadata != null)
            requestBody.metadata = metadata

        if (serviceTier != null)
            requestBody.service_tier = serviceTier

        if (user != null)
            requestBody.user = user

        if (safetyIdentifier != null)
            requestBody.safety_identifier = safetyIdentifier

        if (promptCacheKey != null)
            requestBody.prompt_cache_key = promptCacheKey

        if (promptCacheRetention != null)
            requestBody.prompt_cache_retention =
                    promptCacheRetention

        // Streaming
        if (streamOptions != null)
            requestBody.stream_options = streamOptions

        requestBody
    }


    // ---------------------------------------------------------
    // Chat
    // ---------------------------------------------------------

    String chat(String userMessage) {

        appendUserMessage(userMessage)

        chat()
    }


    String chat() {
        // Prepend system prompt as the first message if not already present
        if (systemPrompt && !messages.find { it.role == 'system' }) {
            messages.add(0, [role: 'system', content: systemPrompt])
        }

        def requestBody = buildRequestBody()
        def jsonBody = JsonOutput.toJson(requestBody)

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

        conn.connectTimeout = connectTimeout
        conn.readTimeout = readTimeout

        conn.setRequestProperty(
                'Content-Type',
                contentType
        )

        conn.setRequestProperty(
                'Authorization',
                "Bearer ${getEffectiveApiKey()}"
        )

        conn.outputStream.withWriter('UTF-8') { writer ->
            writer.write(jsonBody)
        }

        int statusCode = conn.responseCode

        def responseText

        if (statusCode >= 200 && statusCode < 300) {

            responseText =
                    conn.inputStream.getText('UTF-8')

        } else {

            responseText =
                    conn.errorStream?.getText('UTF-8') ?: ''

            conn.disconnect()

            throw new RuntimeException(
                    "LLM API request failed with HTTP " +
                            "${statusCode}: ${responseText}"
            )
        }

        conn.disconnect()

        if (debug) {
            println '=== LLM Response ==='
            println responseText
            println '===================='
        }

        def parsed =
                new JsonSlurper().parseText(responseText)

        def assistantContent =
                parsed?.choices?.getAt(0)?.message?.content
                        ?.toString() ?: ''

        appendAssistantMessage(
                assistantContent
        )

        assistantContent
    }


    // ---------------------------------------------------------
    // Aliases
    // ---------------------------------------------------------

    String send(String userMessage) {
        chat(userMessage)
    }

    String ask(String userMessage) {
        chat(userMessage)
    }

    String generate(String userMessage) {
        chat(userMessage)
    }
}