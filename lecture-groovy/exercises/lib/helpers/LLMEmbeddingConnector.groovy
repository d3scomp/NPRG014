package helpers

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Helper class that encapsulates the configuration and logic for connecting
 * to Ollama's embedding API.
 *
 * Unlike LLMChatConnector, this class is stateless: each operation independently
 * converts text into a vector embedding.
 *
 * The main operations are:
 *
 *   embedding(text)              -> List<Double>
 *   similarity(text1, text2)     -> double
 *
 * Similarity is calculated locally using cosine similarity between the two
 * embedding vectors returned by Ollama.
 */
class LLMEmbeddingConnector {

    String baseUrl = 'http://localhost:11434/api/embed'

    // Small and lightweight embedding model.
    String model = 'all-minilm'

    String contentType = 'application/json'

    boolean debug = false

    LLMEmbeddingConnector() {
    }

    LLMEmbeddingConnector(String baseUrl, String model) {
        this.baseUrl = baseUrl
        this.model = model
    }

    /**
     * Generate an embedding vector for the supplied text.
     *
     * @param text text to embed
     * @return embedding vector as a List<Double>
     */
    List<Double> embedding(String text) {

        if (text == null) {
            throw new IllegalArgumentException('Text must not be null')
        }

        def requestBody = [
                model: model,
                input: text
        ]

        def response = post(requestBody)

        def embeddings = response?.embeddings

        if (!(embeddings instanceof List) || embeddings.isEmpty()) {
            throw new RuntimeException(
                    "Ollama returned no embedding for the supplied text"
            )
        }

        // /api/embed returns a list of embeddings because it also supports
        // embedding multiple input strings in one request.
        def vector = embeddings[0]

        vector.collect { it as Double }
    }

    /**
     * Generate an embedding vector for the supplied text.
     *
     * @param text text to embed
     * @return embedding vector as a List<Double>
     */
    List<List<Double>> embeddings(List<String> texts) {
        if (texts == null) {
            throw new IllegalArgumentException('Texts must not be null')
        }
    
        return texts.collect{embedding(it)}
    }

    /**
     * Calculate cosine similarity between two pieces of text.
     *
     * The texts are first converted to embeddings using Ollama.
     *
     * Result:
     *
     *   1.0  -> identical direction in embedding space
     *   0.0  -> orthogonal / unrelated direction
     *  -1.0  -> opposite direction
     *
     * In practice, for many text embedding models, semantically similar
     * texts normally produce positive values relatively close to 1.
     *
     * @param text1 first text
     * @param text2 second text
     * @return cosine similarity
     */
    double similarity(String text1, String text2) {

        List<Double> v1 = embedding(text1)
        List<Double> v2 = embedding(text2)

        cosineSimilarity(v1, v2)
    }

    /**
     * Calculate cosine similarity between two already-generated vectors.
     *
     * This method is useful when many comparisons have to be made against
     * the same texts: generate their embeddings once and compare the vectors
     * locally instead of repeatedly calling Ollama.
     */
    double cosineSimilarity(List<Double> v1, List<Double> v2) {

        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException(
                    'Embedding vectors must not be null'
            )
        }

        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException(
                    "Embedding dimensions differ: ${v1.size()} vs ${v2.size()}"
            )
        }

        double dotProduct = 0.0
        double norm1 = 0.0
        double norm2 = 0.0

        for (int i = 0; i < v1.size(); i++) {
            double a = v1[i]
            double b = v2[i]

            dotProduct += a * b
            norm1 += a * a
            norm2 += b * b
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            throw new IllegalArgumentException(
                    'Cannot calculate cosine similarity for a zero vector'
            )
        }

        dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2))
    }

    List<String> splitIntoChunks(String text, int maxChars = 1000) {
    
        if (!text) {
            return []
        }
    
        if (maxChars < 1) {
            throw new IllegalArgumentException("maxChars must be greater than 0")
        }
    
        // Split approximately at sentence boundaries.
        def sentences = text
            .replaceAll(/\s+/, ' ')
            .trim()
            .split(/(?<=[.!?])\s+/)
    
        List<String> chunks = []
        StringBuilder current = new StringBuilder()
    
        sentences.each { sentence ->
    
            if (current.length() == 0) {
                current.append(sentence)
            }
            else if (current.length() + 1 + sentence.length() <= maxChars) {
                current.append(' ').append(sentence)
            }
            else {
                chunks << current.toString()
                current = new StringBuilder(sentence)
            }
        }
    
        if (current.length() > 0) {
            chunks << current.toString()
        }
    
        chunks
    }
    /**
     * Send a request to Ollama's embedding endpoint.
     */
    private Map post(Map requestBody) {

        def jsonBody = JsonOutput.toJson(requestBody)

        if (debug) {
            println '=== Embedding Request ==='
            println "URL: ${baseUrl}"
            println "Body: ${jsonBody}"
            println '========================='
        }

        HttpURLConnection conn =
                (HttpURLConnection) new URL(baseUrl).openConnection()

        conn.requestMethod = 'POST'
        conn.doOutput = true
        conn.setRequestProperty('Content-Type', contentType)

        conn.outputStream.withWriter('UTF-8') { writer ->
            writer.write(jsonBody)
        }

        int responseCode = conn.responseCode

        def responseStream =
                responseCode >= 200 && responseCode < 300 ?
                        conn.inputStream :
                        conn.errorStream

        def responseText = responseStream?.getText('UTF-8') ?: ''

        conn.disconnect()

        if (debug) {
            println '=== Embedding Response ==='
            println responseText
            println '=========================='
        }

        if (responseCode < 200 || responseCode >= 300) {
            throw new RuntimeException(
                    "Ollama embedding request failed (${responseCode}): ${responseText}"
            )
        }

        new JsonSlurper().parseText(responseText) as Map
    }
}