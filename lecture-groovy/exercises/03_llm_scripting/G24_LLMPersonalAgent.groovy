#!/usr/bin/env groovy

/*
 * LLM Personal Agent
 *
 * =============================================================================
 * HIGH-LEVEL ARCHITECTURE
 * =============================================================================
 *
 * The application consists of a GUI and several specialized LLM interactions.
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
 *   │  Selects only user information      │
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
 *   │  (Automatically handles MCP tools)  │
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
 *               ┌──────┴──────┐
 *               │             │
 *           CORRECT         INVALID
 *               │             │
 *               │             └──────> ask main LLM again
 *               │                      (maximum 3 attempts)
 *               ▼
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
 *   │        learntUserInfo.md            │
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
 * It is run independently for EVERY user request because relevance depends
 * on what the user is asking about right now.
 *
 * For example, the same stored user information can result in different
 * relevant bits for:
 *
 *     "Suggest a quick snack for next Sunday afternoon"
 *
 * and:
 *
 *     "Which programming language should I learn?"
 *
 * The persistent information is therefore filtered dynamically according
 * to the current prompt.
 *
 *
 * THREADING:
 *
 * Swing GUI operations must happen on Swing's Event Dispatch Thread (EDT).
 *
 * Potentially slow LLM calls are performed in a background thread.
 *
 * GUI updates are sent back to the EDT using SwingUtilities.invokeLater().
 *
 * The Send button is disabled while one request is being processed.
 *
 *
 * GROOVY SCRIPT SCOPE:
 *
 * Variables used by methods defined in a Groovy script must be explicit
 * script fields when they are initialized as script variables.
 *
 * Therefore files, connectors, and Swing components accessed by methods
 * are declared with @Field below.
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
 * System prompt for the request-specific information retrieval LLM.
 *
 * This LLM does NOT answer the user's question.
 *
 * It receives the persistent user information AND the current request.
 * It selects only information that is relevant to the current request.
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
 * This LLM runs AFTER the answer has been produced.
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
// @Field is important here.
//
// handleUserMessage(), retrieveRelevantBits(), and saveLearntUserInfo()
// are methods of the generated Groovy script class. They therefore need
// these objects to be actual script fields rather than local variables.
//

@Field File preferencesFile = new File(
        'personalAgent/preferences.md'
)

if (!preferencesFile.exists()) {

    preferencesFile = new File(
            new File(
                    getClass().protectionDomain.codeSource.location.path
            ).parentFile,
            'personalAgent/preferences.md'
    )
}


@Field String preferences =
        preferencesFile.exists()
                ? preferencesFile.text.trim()
                : ''


@Field File learntInfoFile = new File(
        'personalAgent/learntUserInfo.md'
)

if (!learntInfoFile.exists() &&
        !learntInfoFile.parentFile.exists()) {

    learntInfoFile = new File(
            new File(
                    getClass().protectionDomain.codeSource.location.path
            ).parentFile,
            'personalAgent/learntUserInfo.md'
    )
}


// ── LLM Connectors ───────────────────────────────────────────────────────────
//
// These are also @Field variables because they are accessed by methods.
//

/*
 * MAIN LLM
 *
 * Generates the actual answer to the user's request.
 */
@Field def connector =
        new LLMChatConnectorWithTools(
                debug: true,
                model: 'qwen3.6',
                systemPrompt: PROMPT_MAIN_AGENT
        )

/*
 * REGISTER MCP TOOLS
 *
 * Registering the tool on the connector. The enhanced LLMChatConnector will 
 * handle the execution of this closure automatically during its internal loop.
 */
connector.registerTool(
        "fetchWebPageContent",
        "Fetches the HTML content of a given web page URL.",
        [
                type: "object",
                properties: [
                        urlAddress: [
                                type: "string",
                                description: "The URL of the web page to fetch"
                        ]
                ],
                required: ["urlAddress"]
        ]
) { args ->
    return fetchWebPageContent(args.urlAddress as String)
}


/*
 * RELEVANT-BITS LLM
 *
 * Called ONCE FOR EACH USER REQUEST.
 *
 * The current user request is supplied to this connector.
 */
@Field def relevantBitsConnector =
        new LLMChatConnector(
                debug: false,
                model: 'qwen3.6',
                systemPrompt: PROMPT_RELEVANT_BITS
        )


/*
 * INFORMATION-EXTRACTION LLM
 *
 * Identifies new enduring facts about the user after the response.
 */
@Field def infoExtractionConnector =
        new LLMChatConnector(
                debug: false,
                model: 'qwen3.6',
                systemPrompt: PROMPT_INFO_EXTRACTION
        )


// ── GUI State ────────────────────────────────────────────────────────────────

/*
 * Swing components are explicit @Field variables.
 *
 * This allows both the GUI-building closure and ordinary script methods
 * such as handleUserMessage() to access the same components.
 */
@Field JFrame mainFrame = null
@Field JTextArea chatArea = null
@Field JTextArea inputField = null
@Field JButton sendButton = null

@Field SwingBuilder swing = new SwingBuilder()


// ── Build GUI ────────────────────────────────────────────────────────────────

swing.edt {

    mainFrame = frame(
            title: 'LLM Personal Agent',
            size: [640, 480],
            locationByPlatform: true
    ) {

        borderLayout()


        // ── Central chat history display ────────────────────────────────────

        scrollPane(
                constraints: BorderLayout.CENTER,
                verticalScrollBarPolicy:
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                horizontalScrollBarPolicy:
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        ) {

            /*
             * Explicit assignment to the @Field.
             */
            chatArea = textArea(
                    text: '',
                    editable: false,
                    lineWrap: true,
                    wrapStyleWord: true,
                    font: new Font(
                            'Monospaced',
                            Font.PLAIN,
                            13
                    ),
                    background: new Color(
                            245,
                            245,
                            245
                    )
            )
        }


        // ── Bottom input area ───────────────────────────────────────────────

        panel(
                constraints: BorderLayout.SOUTH,
                border: new EmptyBorder(
                        8,
                        8,
                        8,
                        8
                )
        ) {

            borderLayout()


            inputField = textArea(
                    rows: 3,
                    lineWrap: true,
                    wrapStyleWord: true
            )


            panel(
                    constraints: BorderLayout.EAST
            ) {

                flowLayout(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                8,
                                0
                        )
                )


                // ── Send button ─────────────────────────────────────────────

                sendButton = button(
                        text: 'Send',
                        enabled: true,

                        actionPerformed: {

                            String message =
                                    inputField.text.trim()


                            if (message) {

                                chatArea.append(
                                        "user: ${message}\n"
                                )

                                inputField.text = ''

                                /*
                                 * Prevent concurrent requests.
                                 */
                                sendButton.enabled = false


                                /*
                                 * Run the complete LLM pipeline in the
                                 * background so that the Swing EDT remains
                                 * responsive.
                                 */
                                Thread.start {

                                    handleUserMessage(message)
                                }
                            }
                        }
                )
            }
        }
    }


    mainFrame.show()
}


// ── Message Pipeline ─────────────────────────────────────────────────────────

/*
 * Handles ONE complete user request.
 *
 * ---------------------------------------------------------------------------
 * PER-REQUEST PIPELINE
 * ---------------------------------------------------------------------------
 *
 * 1. Read the latest persistent files.
 *
 * 2. retrieveRelevantBits()
 *       │
 *       └──> relevant-bits LLM
 *             receives:
 *                - preferences
 *                - all learned information
 *                - CURRENT user request
 *
 * 3. Build prompt for main LLM:
 *
 *       preferences
 *       + relevant information
 *       + current request
 *
 * 4. Main LLM generates an answer.
 *
 * 5. Validator LLM checks the answer.
 *
 * 6. If INVALID, retry the main LLM.
 *
 * 7. Display the answer.
 *
 * 8. Information-extraction LLM analyzes the interaction.
 *
 * 9. New facts are saved to learntUserInfo.md.
 */
def handleUserMessage(String message) {

    try {

        // ────────────────────────────────────────────────────────────────────
        // (1) Read the latest persistent information
        // ────────────────────────────────────────────────────────────────────

        String currentPreferences =
                preferencesFile.exists()
                        ? preferencesFile.text.trim()
                        : preferences


        String allLearntInfo =
                learntInfoFile.exists()
                        ? learntInfoFile.text.trim()
                        : ''


        // ────────────────────────────────────────────────────────────────────
        // (2) Request-specific relevant-information retrieval
        // ────────────────────────────────────────────────────────────────────
        //
        // This happens for EVERY request.
        //
        // The current user message is explicitly supplied to the retrieval LLM.
        //

        println(
                'Retrieving relevant user information for current request -----'
        )


        String currentRelevantInfo =
                retrieveRelevantBits(
                        message,
                        currentPreferences,
                        allLearntInfo
                )


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
         * The current user request is always included.
         */
        promptParts << message


        String currentRequest =
                promptParts.join('\n\n')


        // ────────────────────────────────────────────────────────────────────
        // (4) Main LLM + validation loop
        // ────────────────────────────────────────────────────────────────────

        int counter = 0

        final int MAX_COUNT = 3

        String reply = ''


        do {

            println(
                    'Asking main LLM -------------------------------'
            )

            /*
             * The new LLMChatConnector internally orchestrates the 
             * agent loop, intercepts tool execution requests, calls the
             * registered groovy closure, and sends the results back to the LLM.
             */
            String answer =
                    connector.ask(currentRequest)


            reply =
                    answer


            println(
                    "$message : $reply"
            )


            // ────────────────────────────────────────────────────────────────
            // Validate the proposed answer
            // ────────────────────────────────────────────────────────────────

            println(
                    'Validating --------------------------------'
            )


            String validationAnswer =
                    validate(
                            reply,
                            message,
                            currentPreferences,
                            currentRelevantInfo
                    )


            println(
                    "Validation: $validationAnswer"
            )


            if (validationAnswer.trim().startsWith('INVALID')) {

                /*
                 * Ask the main LLM to correct the response.
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

            throw new IllegalArgumentException(
                    "The supplied response is not valid: $reply"
            )
        }


        // ────────────────────────────────────────────────────────────────────
        // (5) Display reply on the Swing EDT
        // ────────────────────────────────────────────────────────────────────

        SwingUtilities.invokeLater {

            chatArea.append(
                    "assistant: ${reply}\n"
            )


            chatArea.setCaretPosition(
                    chatArea.document.length
            )


            sendButton.enabled =
                    true
        }


        // ────────────────────────────────────────────────────────────────────
        // (6) Extract new user facts
        // ────────────────────────────────────────────────────────────────────

        try {

            println(
                    'Extracting new user info ------------------'
            )


            infoExtractionConnector.clearMessages()


            String extractedFacts =
                    infoExtractionConnector.ask(
                            """User message:
$message

Assistant reply:
$reply"""
                    )


            if (extractedFacts &&
                    !extractedFacts.trim().equalsIgnoreCase('NONE') &&
                    !extractedFacts.trim().startsWith('NONE')) {

                saveLearntUserInfo(
                        learntInfoFile,
                        extractedFacts
                )

            } else {

                println(
                        'No new user info learned.'
                )
            }

        } catch (Exception extractErr) {

            /*
             * Failure of the optional extraction step does not invalidate
             * an otherwise successful answer.
             */
            println(
                    "Error extracting user info: ${extractErr.message}"
            )
        }


    } catch (Exception e) {

        /*
         * Report any error from the main pipeline to the GUI.
         *
         * This code runs from the background thread, therefore the Swing
         * components are modified through invokeLater().
         */
        SwingUtilities.invokeLater {

            chatArea.append(
                    "assistant: [ERROR] ${e.message}\n"
            )


            sendButton.enabled =
                    true
        }
    }
}


// ── Relevant Information Retrieval ───────────────────────────────────────────

/**
 * Retrieves information relevant to ONE particular user request.
 *
 * This method completely encapsulates the relevantBitsConnector interaction.
 *
 * Parameters:
 *
 *   message
 *       The CURRENT user request.
 *
 *   preferences
 *       Current contents of preferences.md.
 *
 *   learntInfo
 *       Current contents of learntUserInfo.md.
 *
 * The current request is essential because "relevant information" is
 * request-dependent.
 *
 * Returns:
 *
 *   relevant user information as text
 *
 * or:
 *
 *   an empty string if no relevant information was found or retrieval failed.
 *
 *
 * A retrieval failure is deliberately non-fatal. The main LLM can still
 * answer using the explicit preferences and current request.
 */
String retrieveRelevantBits(
        String message,
        String preferences = '',
        String learntInfo = ''
) {

    try {

        /*
         * Start with a clean connector conversation.
         *
         * This prevents an earlier retrieval request from becoming
         * unintended context for the current retrieval request.
         */
        relevantBitsConnector.clearMessages()


        /*
         * IMPORTANT:
         *
         * The CURRENT USER REQUEST is explicitly included here.
         *
         * This is what makes retrieval request-specific.
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


        println(
                'Calling relevant-bits LLM ---------------------'
        )


        String retrieved =
                relevantBitsConnector.ask(
                        retrievalPrompt
                )


        if (retrieved &&
                !retrieved.trim().equalsIgnoreCase('NONE') &&
                !retrieved.trim().startsWith('NONE')) {

            String relevantInfo =
                    retrieved.trim()


            println(
                    "Relevant info for current request:\n$relevantInfo"
            )


            return relevantInfo
        }


        println(
                'No relevant user info found for current request.'
        )


        return ''


    } catch (Exception e) {

        /*
         * Relevant-bit retrieval is an auxiliary step.
         *
         * If it fails, allow the main agent to continue without
         * request-specific user information.
         */
        println(
                "Error retrieving relevant bits: ${e.message}"
        )


        return ''
    }
}


// ── Persistent User Information ──────────────────────────────────────────────

/**
 * Appends newly extracted facts about the user to the persistent
 * information file.
 *
 * Duplicate facts and "NONE" responses are ignored.
 */
void saveLearntUserInfo(
        File file,
        String facts
) {

    String cleanFacts =
            facts.trim()


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


            file.append(
                    trimmed + "\n"
            )


            appended =
                    true
        }
    }


    if (appended) {

        println(
                "Learnt user info saved to ${file.name}:\n$cleanFacts"
        )
    }
}


// ── Response Validation ──────────────────────────────────────────────────────

/**
 * Sends the proposed response to a separate LLM validator.
 *
 * The validator checks whether the response:
 *
 *   - addresses the original user request,
 *   - follows explicit constraints,
 *   - respects user preferences,
 *   - respects relevant user information,
 *   - is coherent,
 *   - avoids contradictions and false claims,
 *   - avoids irrelevant divergence.
 *
 * Returns a string starting with:
 *
 *   CORRECT
 *
 * or:
 *
 *   INVALID
 */
String validate(
        String response,
        String originalRequest,
        String preferences = '',
        String relevantInfo = ''
) {

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
     * The validator is intentionally created for the validation operation.
     */
    def validator =
            new LLMChatConnector(
                    debug: false,
                    model: 'qwen3.6',

                    systemPrompt:
                            """
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
"""
            )


    String answer =
            validator.ask(response)


    return answer
}

String fetchWebPageContent(String urlAddress) {
    if (1 < 5) return "This is a dummy content of the web page $urlAddress. Words: USA, Canada, EU"
    try {
        HttpURLConnection connection = new URL(urlAddress).openConnection() as HttpURLConnection
        
        // Basic configuration to prevent hanging threads
        connection.requestMethod = 'GET'
        connection.connectTimeout = 120000
        connection.readTimeout = 120000

        int responseCode = connection.responseCode

        // Return the body for successful responses
        if (responseCode in 200..299) {
            return connection.inputStream.getText('UTF-8')
        } else {
            // Return the HTTP error code as a string for 4xx/5xx responses
            return responseCode.toString()
        }
    } catch (Exception e) {
        return "ERROR: ${e.message}"
    }
}