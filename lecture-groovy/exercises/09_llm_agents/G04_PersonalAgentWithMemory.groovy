#!/usr/bin/env groovy

/*
 * LLM Personal Agent
 *
 * =============================================================================
 * HIGH-LEVEL ARCHITECTURE
 * =============================================================================
 *
 * The application combines a Swing GUI with several specialized LLM calls.
 *
 * Persistent files:
 *
 *   personalAgent/preferences.md
 *       └── Explicit user preferences.
 *
 *   personalAgent/learntUserInfo.md
 *       └── Facts learned from previous interactions.
 *
 *
 * EACH TIME THE USER PRESSES "SEND", THE FOLLOWING PIPELINE IS EXECUTED:
 *
 *
 *   ┌──────────────────────┐
 *   │      USER / GUI      │
 *   │  enters a request    │
 *   └──────────┬───────────┘
 *              │
 *              │ current user request
 *              ▼
 *   ┌─────────────────────────────────────┐
 *   │  Read persistent user information   │
 *   │                                     │
 *   │  preferences.md                     │
 *   │  learntUserInfo.md                  │
 *   └─────────────────┬───────────────────┘
 *                     │
 *                     │ current request +
 *                     │ persistent information
 *                     ▼
 *   ┌─────────────────────────────────────┐
 *   │     RELEVANT-BITS LLM               │
 *   │                                     │
 *   │  Selects only the user information  │
 *   │  relevant to THIS request.          │
 *   └─────────────────┬───────────────────┘
 *                     │
 *                     │ relevant information
 *                     ▼
 *   ┌─────────────────────────────────────┐
 *   │        BUILD MAIN PROMPT            │
 *   │                                     │
 *   │  preferences                        │
 *   │  + relevant user information        │
 *   │  + current user request             │
 *   └─────────────────┬───────────────────┘
 *                     │
 *                     ▼
 *   ┌─────────────────────────────────────┐
 *   │          MAIN LLM                   │
 *   │                                     │
 *   │  Generates the actual answer.       │
 *   └─────────────────┬───────────────────┘
 *                     │
 *                     │ proposed answer
 *                     ▼
 *   ┌─────────────────────────────────────┐
 *   │        VALIDATOR LLM                │
 *   │                                     │
 *   │  Checks whether the answer          │
 *   │  correctly addresses the request    │
 *   │  and respects the available user    │
 *   │  context.                           │
 *   └─────────────────┬───────────────────┘
 *                     │
 *              ┌──────┴──────┐
 *              │             │
 *          CORRECT         INVALID
 *              │             │
 *              │             └──────> ask main LLM again
 *              │                       (maximum 3 attempts)
 *              ▼
 *   ┌─────────────────────────────────────┐
 *   │          DISPLAY ANSWER             │
 *   │             IN GUI                  │
 *   └─────────────────┬───────────────────┘
 *                     │
 *                     ▼
 *   ┌─────────────────────────────────────┐
 *   │     INFORMATION-EXTRACTION LLM      │
 *   │                                     │
 *   │  Looks for new enduring facts       │
 *   │  learned about the user.            │
 *   └─────────────────┬───────────────────┘
 *                     │
 *                     │ new facts
 *                     ▼
 *   ┌─────────────────────────────────────┐
 *   │       learntUserInfo.md             │
 *   │                                     │
 *   │  Facts are persisted for use by     │
 *   │  future requests.                   │
 *   └─────────────────────────────────────┘
 *
 *
 * IMPORTANT DESIGN DECISION:
 *
 * The RELEVANT-BITS LLM is NOT run during application startup.
 *
 * Instead, it runs independently for every user request because relevance
 * depends on what the user is asking about right now.
 *
 * For example, the same stored user information can yield different
 * relevant bits for:
 *
 *     "Suggest a quick snack for next Sunday afternoon"
 *
 * and:
 *
 *     "Which programming language should I learn?"
 *
 * The persistent information is therefore filtered dynamically, according
 * to the current request.
 *
 *
 * THREADING:
 *
 * Swing GUI operations must run on Swing's Event Dispatch Thread (EDT).
 * Potentially slow LLM calls are performed in a background thread,
 * and GUI updates are posted back to the EDT via SwingUtilities.invokeLater().
 *
 * The Send button is disabled while a request is being processed.
 *
 *
 * GROOVY SCRIPT SCOPE:
 *
 * In a Groovy script, variables that are accessed by script-level methods
 * must be declared as script fields using the @Field annotation.
 *
 * Accordingly, files, connectors, and Swing components that are shared
 * across methods are declared with @Field below.
 */

// ── Imports ──────────────────────────────────────────────────────────────────

import helpers.*
import groovy.swing.SwingBuilder
import groovy.transform.Field

import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JButton
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder

import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font

// ── Prompt Templates ─────────────────────────────────────────────────────────

@Field final String PROMPT_MAIN_AGENT =
        'You are a helpful personal agent.'

/*
 * System prompt for the request-specific information-retrieval LLM.
 *
 * This LLM does NOT answer the user's question.
 * Instead, it receives the persistent user information together with
 * the current request and selects only the information relevant to it.
 */
@Field final String PROMPT_RELEVANT_BITS = '''
You are an information retrieval and consolidation assistant.

Given the user's recorded preferences, conversation history / learned user
information, and the user's current request, identify the key information
about the user that is relevant to answering THIS CURRENT REQUEST.

Rules:
1. Consider the current user request when deciding which facts and preferences
   are relevant.
2. Extract and consolidate the key facts, preferences, constraints, and
   background details that may affect the answer to the current request.
3. Do not include unrelated user information merely because it exists.
4. If no facts or preferences are relevant to the current request, reply
   strictly with "NONE".
5. Output the relevant bits as concise bullet points (starting with "- ").
6. Do not add preamble or explanations.
'''

/*
 * System prompt for the information-extraction LLM.
 *
 * This LLM runs after the final answer has been produced.
 */
@Field final String PROMPT_INFO_EXTRACTION = '''
You are a personal information extraction assistant.
Analyze the interaction between the user and the assistant to identify any new, specific facts learned about the user (such as user preferences, habits, family, location, occupation, plans, constraints, likes/dislikes).

Rules:
1. Extract only enduring facts or preferences specifically about the user.
2. Do not extract general conversation, temporary queries, or assistant information.
3. If no specific facts about the user were revealed in the conversation, respond strictly with "NONE".
4. If new facts are found, output each fact as a bullet point starting with "- " (e.g. "- User lives in Brno", "- User has two children").
5. Do not include any introductory or concluding text, only the bullet points or "NONE".
'''

// ── Persistent Files ─────────────────────────────────────────────────────────
//
// These variables are declared with @Field because handleUserMessage(),
// retrieveRelevantBits(), and saveLearntUserInfo() are script-level methods
// that need access to them as script fields rather than local variables.
//

@Field File preferencesFile = new File('personalAgent/preferences.md')

if (!preferencesFile.exists()) {
    preferencesFile = new File(new File('./lecture-groovy/exercises/09_llm_agents'), 'personalAgent/preferences.md')
}

@Field String preferences =
        preferencesFile.exists()
                ? preferencesFile.text.trim()
                : ''

@Field File learntInfoFile = new File('personalAgent/learntUserInfo.md')

if (!learntInfoFile.exists() &&
        !learntInfoFile.parentFile.exists()) {
    learntInfoFile = new File(new File('./lecture-groovy/exercises/09_llm_agents'), 'personalAgent/learntUserInfo.md')
}

// ── LLM Connectors ───────────────────────────────────────────────────────────
//
// These connectors are also declared with @Field so that script-level
// methods can access them.
//

/*
 * Main LLM — generates the answer to the user's request.
 */
@Field def connector = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: PROMPT_MAIN_AGENT)

/*
 * Relevant-bits LLM — called once per user request.
 *
 * The current user request is passed to this connector so that it can
 * determine which stored facts are relevant.
 */
@Field def relevantBitsConnector = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: PROMPT_RELEVANT_BITS)

/*
 * Information-extraction LLM — identifies new enduring facts about
 * the user after the response has been produced.
 */
@Field def infoExtractionConnector = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: PROMPT_INFO_EXTRACTION)

// ── GUI State ────────────────────────────────────────────────────────────────

/*
 * Swing components are declared as @Field variables so that both the
 * GUI-building closure and script-level methods such as handleUserMessage()
 * can access the same component instances.
 */
@Field JFrame mainFrame = null
@Field JTextArea chatArea = null
@Field JTextArea inputField = null
@Field JButton sendButton = null

@Field SwingBuilder swing = new SwingBuilder()

// ── Build GUI ────────────────────────────────────────────────────────────────

swing.edt {
    mainFrame = frame(title: 'LLM Personal Agent', size: [640, 480], locationByPlatform: true) {

        borderLayout()

        // ── Central chat history display ────────────────────────────────────

        scrollPane(constraints: BorderLayout.CENTER, verticalScrollBarPolicy: JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
            /*
             * Assign the text area to the @Field variable so that
             * script-level methods can access it.
             */
            chatArea = textArea(text: '', editable: false, lineWrap: true, wrapStyleWord: true, font: new Font('Monospaced', Font.PLAIN, 13), background: new Color(245, 245, 245))
        }

        // ── Bottom input area ───────────────────────────────────────────────

        panel(constraints: BorderLayout.SOUTH, border: new EmptyBorder(8, 8, 8, 8)) {
            borderLayout()

            inputField = textArea(rows: 3, lineWrap: true, wrapStyleWord: true)
            panel(constraints: BorderLayout.EAST) {
                flowLayout(new FlowLayout(FlowLayout.CENTER, 8, 0))

                // ── Send button ─────────────────────────────────────────────

                sendButton = button(text: 'Send', enabled: true, actionPerformed: {
                            String message = inputField.text.trim()
                            if (message) {
                                chatArea.append("user: ${message}\n")
                                inputField.text = ''
                                sendButton.enabled = false
                                Thread.start {
                                    handleUserMessage(message)
                                }
                            }
                        })
            }
        }
    }
    mainFrame.show()
}

// ── Message Pipeline ─────────────────────────────────────────────────────────

/*
 * Handles a single user request end to end.
 *
 * ---------------------------------------------------------------------------
 * PER-REQUEST PIPELINE
 * ---------------------------------------------------------------------------
 *
 * 1. Read the latest persistent files.
 *
 * 2. Call retrieveRelevantBits(), which invokes the relevant-bits LLM
 *    with the user's preferences, all learned information, and the
 *    current request.
 *
 * 3. Build the prompt for the main LLM from the preferences,
 *    the relevant information, and the current request.
 *
 * 4. The main LLM generates an answer.
 *
 * 5. A validator LLM checks the answer.
 *
 * 6. If the answer is INVALID, retry the main LLM (up to three attempts).
 *
 * 7. Display the answer in the GUI.
 *
 * 8. An information-extraction LLM analyzes the interaction for new facts.
 *
 * 9. Any new facts are saved to learntUserInfo.md.
 */
def handleUserMessage(String message) {

    try {
        // ────────────────────────────────────────────────────────────────────
        // (1) Read the latest persistent information
        // ────────────────────────────────────────────────────────────────────
        String currentPreferences = preferencesFile.exists() ? preferencesFile.text.trim() : preferences

        String allLearntInfo = learntInfoFile.exists() ? learntInfoFile.text.trim() : ''

        // ────────────────────────────────────────────────────────────────────
        // (2) Retrieve the user information relevant to this specific request
        // ────────────────────────────────────────────────────────────────────
        //
        // This step runs for every request because relevance depends on
        // what the user is currently asking about.
        //
        println('Retrieving relevant user information for current request -----')
        String currentRelevantInfo = retrieveRelevantBits(message, currentPreferences, allLearntInfo)
        // ────────────────────────────────────────────────────────────────────
        // (3) Build the main-agent prompt
        // ────────────────────────────────────────────────────────────────────
        def promptParts = []
        if (currentPreferences) {
            promptParts << """User preferences:
---------------
$currentPreferences
---------------"""
        }

        if (currentRelevantInfo) {
            promptParts << """Relevant user information for this request:
---------------
$currentRelevantInfo
---------------"""
        }

        /*
         * The user's current request is always appended to the prompt.
         */
        promptParts << message
        String currentRequest = promptParts.join('\n\n')
        // ────────────────────────────────────────────────────────────────────
        // (4) Main LLM + validation loop
        // ────────────────────────────────────────────────────────────────────
        int counter = 0
        final int MAX_COUNT = 3
        String reply = ''
        do {
            println('Asking main LLM -------------------------------')
            String answer = connector.ask(currentRequest)
            reply = answer
            println("$message : $reply")
            // ────────────────────────────────────────────────────────────────
            // Validate the answer produced by the main LLM
            // ────────────────────────────────────────────────────────────────
            println('Validating --------------------------------')
            String validationAnswer = validate(reply, message, currentPreferences, currentRelevantInfo)
            println("Validation: $validationAnswer")

            if (validationAnswer.trim().startsWith('INVALID')) {
                /*
                 * The validator flagged the answer as invalid.
                 * Ask the main LLM to produce a corrected response.
                 */
                currentRequest =
                        """Please correct the response.

Validation failed. Result:
$validationAnswer

Original user request:
$message

Please provide a corrected response."""
                counter++
            } else {
                break
            }
        } while (counter < MAX_COUNT)
        if (counter >= MAX_COUNT) {
            throw new IllegalArgumentException("The supplied response is not valid: $reply")
        }

        // ────────────────────────────────────────────────────────────────────
        // (5) Display the reply on the Swing EDT
        // ────────────────────────────────────────────────────────────────────
        SwingUtilities.invokeLater {
            chatArea.append("assistant: ${reply}\n")
            chatArea.setCaretPosition(chatArea.document.length)
            sendButton.enabled = true
        }

        // ────────────────────────────────────────────────────────────────────
        // (6) Extract any new facts about the user from this interaction
        // ────────────────────────────────────────────────────────────────────
        
    // TASK 1
    // 1. Replace the contents of `learntUserInfo.md` with a single line: "- User likes France".
    // 2. Send the message: "I like France very much. Any tips for a two-day trip to Paris?"
    // 3. Wait for the response.
    // 4. Inspect `learntUserInfo.md` and observe that the fact is stored twice.

    // TASK 2
    // Prevent duplicates by ensuring the information-extraction LLM is aware of the facts
    // already stored in the preferences file and the memory file.
    // The `allLearntInfo` variable holds the current content of the memory file.
    // The `currentPreferences` variable holds the current content of the preferences file.    
    // Re-run the scenario above to verify that the duplicate is no longer saved.
        
        try {
            println('Extracting new user info ------------------')
            infoExtractionConnector.clearMessages()
            String extractedFacts = infoExtractionConnector.ask("""User message:
$message

Assistant reply:
$reply
""")
            if (extractedFacts &&
                    !extractedFacts.trim().equalsIgnoreCase('NONE') &&
                    !extractedFacts.trim().startsWith('NONE')) {
                saveLearntUserInfo(learntInfoFile, extractedFacts)
            } else {
                println('No new user info learned.')
            }

        } catch (Exception extractErr) {
            /*
             * The extraction step is optional: a failure here should not
             * invalidate an otherwise successful answer.
             */
            println("Error extracting user info: ${extractErr.message}")
        }

    } catch (Exception e) {
        /*
         * Report pipeline errors in the GUI.
         *
         * Because this code runs on a background thread, Swing components
         * are updated through invokeLater().
         */
        SwingUtilities.invokeLater {
            chatArea.append("assistant: [ERROR] ${e.message}\n")
            sendButton.enabled = true
        }
    }
}


// ── Relevant Information Retrieval ───────────────────────────────────────────

/**
 * Retrieves the user information relevant to a single user request.
 *
 * This method encapsulates the entire interaction with the relevant-bits LLM.
 *
 * Parameters:
 *
 *   message
 *       The current user request.
 *
 *   preferences
 *       The current contents of preferences.md.
 *
 *   learntInfo
 *       The current contents of learntUserInfo.md.
 *
 * The current request is essential because relevance is request-dependent.
 *
 * Returns:
 *
 *   The relevant user information as plain text, or an empty string if no
 *   relevant information was found or if retrieval failed.
 *
 * A retrieval failure is deliberately non-fatal: the main LLM can still
 * answer using the explicit preferences and the current request.
 */
String retrieveRelevantBits(String message, String preferences = '', String learntInfo = '') {
    try {
        /*
         * Clear any previous messages so that an earlier retrieval request
         * does not become unintended context for the current one.
         */
        relevantBitsConnector.clearMessages()

        /*
         * The current user request is explicitly included in the prompt.
         * This is what makes the retrieval specific to the current request.
         */
        String retrievalPrompt =
                """User preferences:
${preferences ?: 'None'}

Conversation history and learned user information:
${learntInfo ?: 'None'}

Current user request:
$message

Please retrieve and consolidate only the user information and preferences
that are relevant to answering this current request."""

        println('Calling relevant-bits LLM ---------------------')

        String retrieved = relevantBitsConnector.ask(retrievalPrompt)

        if (retrieved &&
                !retrieved.trim().equalsIgnoreCase('NONE') &&
                !retrieved.trim().startsWith('NONE')) {
            String relevantInfo =
                    retrieved.trim()

            println("Relevant info for current request:\n$relevantInfo")

            return relevantInfo
        }

        println('No relevant user info found for current request.')

        return ''

    } catch (Exception e) {
        /*
         * Retrieval is an auxiliary step: if it fails, the main agent
         * continues without request-specific user information.
         */
        println("Error retrieving relevant bits: ${e.message}")

        return ''
    }
}

// ── Persistent User Information ──────────────────────────────────────────────

/**
 * Appends newly extracted facts about the user to the persistent
 * information file, skipping duplicates and "NONE" responses.
 */
void saveLearntUserInfo(
        File file,
        String facts) {
    String cleanFacts = facts.trim()

    if (!cleanFacts ||
            cleanFacts.equalsIgnoreCase('NONE') ||
            cleanFacts.startsWith('NONE')) {
        return
    }

    file.parentFile?.mkdirs()

    if (!file.exists()) {
        file.text =
                "# Learnt User Information\n\n"
    }

    String existingText =
            file.text

    boolean appended =
            false

    cleanFacts.eachLine { line ->
        String trimmed =
                line.trim()

        if (trimmed &&
                !trimmed.equalsIgnoreCase('NONE') &&
                !existingText.contains(trimmed)) {
            if (!trimmed.startsWith('-') &&
                    !trimmed.startsWith('*')) {
                trimmed =
                        "- " + trimmed
            }

            file.append(trimmed + "\n")

            appended =
                    true
        }
    }

    if (appended) {
        println("Learnt user info saved to ${file.name}:\n$cleanFacts")
    }
}

// ── Response Validation ──────────────────────────────────────────────────────

/**
 * Sends the proposed response to a separate LLM for validation.
 *
 * The validator checks whether the response addresses the original request,
 * follows explicit constraints, respects user preferences and known facts,
 * and remains coherent without contradictions or irrelevant divergence.
 *
 * Returns a string that starts with either "CORRECT" or "INVALID".
 */
String validate(String response, String originalRequest, String preferences = '', String relevantInfo = '') {
    String contextSection =
            ''

    if (preferences) {
        contextSection += """
User preferences:
---------------
$preferences
---------------
"""
    }

    if (relevantInfo) {
        contextSection += """
Relevant user information:
---------------
$relevantInfo
---------------
"""
    }

    /*
     * A fresh validator connector is created for each validation call
     * so that previous validation conversations do not interfere.
     */
    def validator = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: """
You validate whether the provided response correctly and appropriately
addresses the user request.

${contextSection}

The original user request was:
---------------
$originalRequest
---------------

Evaluate whether the response:
- Directly, accurately, and adequately addresses the user request.
- Fulfills any explicit constraints, instructions, or format requirements
  specified in the request.
- Adheres to and respects the user preferences and known user information.
- Is helpful, coherent, and free of contradictions, false claims, or
  irrelevant divergence.

You must respond with text starting with either 'CORRECT' or 'INVALID'.
A detailed explanation of your decision should follow only if the response
is INVALID.
""")

    String answer = validator.ask(response)
    return answer
}