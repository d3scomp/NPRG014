import helpers.*

class SemanticTextConnector {

    LLMEmbeddingConnector embeddings = new LLMEmbeddingConnector(model: 'nomic-embed-text')

    /**
     * Similarity threshold used by the boolean predicates.
     *
     * This value is model- and application-dependent.
     * It should ideally be calibrated on representative examples.
     */
    double similarityThreshold = 0.70

    private static final Map<String, String> FEELING_PROTOTYPES = [
        enthusiastic:
            'enthusiastic, excited, energetic, strongly positive and eager',
        disappointed:
            'disappointed, dissatisfied, let down, negative reaction',
        angry:
            'angry, irritated, hostile, frustrated and upset',
        calm:
            'calm, peaceful, relaxed and emotionally composed',
        anxious:
            'anxious, worried, nervous, fearful and uneasy',
        humorous:
            'humorous, playful, funny and intended to amuse',
        happy:
            'happy, enthusiastic, playful, fun, energetic, excited, enjoy'
    ]

    /**
     * Returns true if the two texts are semantically similar enough.
     */
    boolean similarTo(String text1, String text2) {
        embeddings.similarity(text1, text2) >= similarityThreshold
    }

    /**
     * Returns the actual similarity value.
     *
     * Useful when the application needs more information than a boolean.
     */
    double similarity(String text1, String text2) {
        embeddings.similarity(text1, text2)
    }

    /**
     * Returns true if the document is semantically similar enough
     * to the supplied topic.
     *
     * Example:
     *
     *   talksAbout(
     *       document,
     *       'quantum computing'
     *   )
     */
    boolean talksAbout(String document, String topic) {
        if (!document || !topic) {
            return false
        }
        
        def chunks = embeddings.splitIntoChunks(document)
        
        chunks.any { chunk ->
            embeddings.similarity(chunk, topic) >= similarityThreshold
        }
    }

    /**
     * Returns true if the text is semantically compatible with
     * the supplied feeling.
     *
     * Example:
     *
     *   feelsLike(review, 'enthusiastic')
     *
     * This is an approximate semantic test rather than a true
     * emotion classifier.
     */
    boolean feelsLike(String text, String feeling) {

        String prototype = FEELING_PROTOTYPES[feeling.toLowerCase()]

        if (!prototype) {
            throw new IllegalArgumentException(
                "Unknown feeling: ${feeling}"
            )
        }

        embeddings.similarity(text, prototype) >= similarityThreshold
    }
}

def sem = new SemanticTextConnector()

println 'Sounds happy: ' + sem.feelsLike("I'm enjoying this and feel excited and happy. So much fun!", "happy")

def providedWord = 'Leash'
println ('Most similar animal: ' + ['Cat', 'Dog', 'Cow', 'Sheep'].max {sem.similarity(it, providedWord)})


if (sem.similarTo(
        "The experiment produced a statistically significant result.",
        "The experiment produced a significant result."
)) {
    println "Similar"
}

// TASK spot the limitation of embedding models below
if (sem.similarTo(
        "The experiment produced a statistically significant result.",
        "The experiment produced no significant result."
)) {
    println "Similar"
}



