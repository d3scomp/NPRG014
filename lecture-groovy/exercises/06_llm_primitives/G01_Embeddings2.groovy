import helpers.*

/**
 * Higher-level semantic operations implemented using text embeddings.
 *
 * This class deliberately separates:
 *
 *   LLMEmbeddingConnector
 *       text -> vector
 *
 * from:
 *
 *   SemanticTextConnector
 *       semantic relationship between texts
 */
class SemanticTextConnector {

    LLMEmbeddingConnector embeddings

    /**
     * Similarity threshold used by the boolean predicates.
     *
     * This value is model- and application-dependent.
     * It should ideally be calibrated on representative examples.
     */
    double similarityThreshold = 0.70

    SemanticTextConnector() {
        embeddings = new LLMEmbeddingConnector(model: 'nomic-embed-text')
    }

    SemanticTextConnector(LLMEmbeddingConnector embeddings) {
        this.embeddings = embeddings
    }

    SemanticTextConnector(
        LLMEmbeddingConnector embeddings,
        double similarityThreshold
    ) {
        this.embeddings = embeddings
        this.similarityThreshold = similarityThreshold
    }

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
            'humorous, playful, funny and intended to amuse'
    ]

    /**
     * Returns true if the two texts are semantically similar enough.
     *
     * Example:
     *
     *   similarTo(
     *       'The cat is sleeping.',
     *       'A kitten is taking a nap.'
     *   )
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

class LLMTextClassifier{    

    /**
     * Returns true if text1 supports text2.
     */
    String summarize(String text) {
    
        def llm = new LLMChatConnector()
        llm.systemPrompt = '''
            When given text, return a short summary of it. Nothing else, just a summary reduced down to the bone.
'''
        def summary = llm.chat(text)
        summary
    }
    
    /**
     * Returns true if text1 supports text2.
     */
    boolean supports(String text1, String text2) {
        classify(text1, text2) == 'SUPPORTS'
    }
    
    /**
     * Returns true if text1 contradicts text2.
     */
    boolean contradicts(String text1, String text2) {
        classify(text1, text2) == 'CONTRADICTS'
    }
    
    /**
     * Classify the relationship of text1 to text2.
     *
     * @return SUPPORTS, CONTRADICTS, or NEUTRAL
     */
    String classify(String text1, String text2) {
    
        if (!text1 || !text2) {
            throw new IllegalArgumentException(
                'Both texts must be supplied'
            )
        }
    
        // Use a fresh conversation for each classification.
        def llm = new LLMChatConnector()
        llm.systemPrompt = '''

You are a text relationship classifier.

Given two texts, classify the relationship of TEXT 1 to TEXT 2.

Return exactly one of these three labels:

SUPPORTS
CONTRADICTS
NEUTRAL

Definitions:

SUPPORTS:
TEXT 1 provides evidence for, entails, or is consistent with TEXT 2.

CONTRADICTS:
TEXT 1 provides evidence against, conflicts with, or entails the opposite of TEXT 2.

NEUTRAL:
TEXT 1 neither supports nor contradicts TEXT 2.

Return ONLY the label. Do not provide an explanation.
'''.trim()
   
        String prompt = """

    TEXT 1:
    ${text1}
    
    TEXT 2:
    ${text2}
    
    Classify the relationship of TEXT 1 to TEXT 2.
    """.trim()
    
        String result = llm.chat(prompt)
            .trim()
            .toUpperCase()
    
        switch (result) {
            case 'SUPPORTS':
            case 'CONTRADICTS':
            case 'NEUTRAL':
                return result
    
            default:
                throw new RuntimeException(
                    "Unexpected relationship returned by LLM: ${result}"
                )
        }
    }    
}

def sem = new SemanticTextConnector()

println sem.feelsLike("I'm worried, very worried", "anxious")

def relations = new LLMTextClassifier()

if (sem.similarTo(
        "The experiment produced a statistically significant result.",
        "The experiment produced a significant result."
)) {
    println "Similar"
}


if (relations.supports(
        "The experiment produced a statistically significant result.",
        "The experiment produced a significant result."
)) {
    println "Supported"
}

if (sem.similarTo(
        "The experiment produced a statistically significant result.",
        "The experiment produced a significant result."
)) {
    println "Similar"
}

if (relations.contradicts(
        "The experiment produced no statistically significant result.",
        "The experiment produced a significant result."
)) {
    println "Contradicted"
}

def providedWord = 'Leash'
['Cat', 'Dog', 'Cow', 'Sheep'].max {sem.similarity(it, providedWord)}

println relations.summarize("The experiment produced no statistically significant result.")
