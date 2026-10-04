import helpers.*

/**
 * HOMEWORK ASSIGNMENT: LLM-Powered Text Relationship Classifier
 * 2026/2027
 *
 * Your task is to implement the `TextClassificationCategory` class so that the 
 * test block at the bottom of this script executes successfully.
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
