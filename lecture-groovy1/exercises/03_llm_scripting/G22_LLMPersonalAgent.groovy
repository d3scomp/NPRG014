#!/usr/bin/env groovy

import helpers.*
import groovy.swing.SwingBuilder

import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder

import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font

File preferencesFile = new File('personalAgent/preferences.md')
if (!preferencesFile.exists()) {
    preferencesFile = new File(new File(getClass().protectionDomain.codeSource.location.path).parentFile, 'personalAgent/preferences.md')
}
preferences = preferencesFile.exists() ? preferencesFile.text.trim() : ''

File learntInfoFile = new File('personalAgent/learntUserInfo.md')
if (!learntInfoFile.exists() && !learntInfoFile.parentFile.exists()) {
    learntInfoFile = new File(new File(getClass().protectionDomain.codeSource.location.path).parentFile, 'personalAgent/learntUserInfo.md')
}

def connector = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: 'You are a helpful personal agent.')

// Separate LLM connector to identify and extract relevant bits from learnt user info and preferences
def relevantBitsConnector = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: '''
You are an information retrieval and consolidation assistant.
Given the user's recorded preferences and conversation history / learned user information, your task is to extract, summarize, and consolidate the key relevant bits of information, preferences, and facts about the user.

Rules:
1. Extract and consolidate the key facts, preferences, constraints, and background details about the user.
2. If no facts or preferences are available, reply strictly with "NONE".
3. Output the extracted bits as concise bullet points (starting with "- "). Do not add preamble or explanations.
''')

// Separate LLM connector to extract new interesting facts learned about the user from the interaction
def infoExtractionConnector = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: '''
You are a personal information extraction assistant.
Analyze the interaction between the user and the assistant to identify any new, specific facts learned about the user (such as user preferences, habits, family, location, occupation, plans, constraints, likes/dislikes).

Rules:
1. Extract only enduring facts or preferences specifically about the user.
2. Do not extract general conversation, temporary queries, or assistant information.
3. If no specific facts about the user were revealed in the conversation, respond strictly with "NONE".
4. If new facts are found, output each fact as a bullet point starting with "- " (e.g. "- User lives in Brno", "- User has two children").
5. Do not include any introductory or concluding text, only the bullet points or "NONE".
''')

def relevantInfo = ''
def mainFrame = null
def swing = new SwingBuilder()

swing.edt {
    mainFrame = frame(
            id: 'mainFrame',
            title: 'Loading used preferences and conversation history',
            size: [640, 480],
            locationByPlatform: true
    ) {
        borderLayout()

        // Central chat history display
        scrollPane(
                constraints: BorderLayout.CENTER,
                verticalScrollBarPolicy: JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        ) {
            textArea(
                    id: 'chatArea',
                    text: '',
                    editable: false,
                    lineWrap: true,
                    wrapStyleWord: true,
                    font: new Font('Monospaced', Font.PLAIN, 13),
                    background: new Color(245, 245, 245)
            )
        }

        // Bottom input area
        panel(
                constraints: BorderLayout.SOUTH,
                border: new EmptyBorder(8, 8, 8, 8)
        ) {
            borderLayout()

            textArea(
                    id: 'inputField',
                    rows: 3,
                    lineWrap: true,
                    wrapStyleWord: true
            ).setName('inputField')

            panel(constraints: BorderLayout.EAST) {
                flowLayout(new FlowLayout(FlowLayout.CENTER, 8, 0))

                button(
                        id: 'sendButton',
                        text: 'Send',
                        enabled: false,
                        actionPerformed: {
                            String message = inputField.text.trim()

                            if (!message) {
                                return
                            }

                            // Update UI immediately
                            chatArea.append("user: ${message}\n")
                            inputField.text = ''
                            sendButton.enabled = false

                            // Do the potentially slow LLM call off the EDT
                            Thread.start {
                                try {
                                    def currentPreferences = preferencesFile.exists() ? preferencesFile.text.trim() : preferences

                                    // Build request with preferences, relevant info right after preferences, and message
                                    def promptParts = []
                                    if (currentPreferences) {
                                        promptParts << """User preferences:
---------------
$currentPreferences
---------------"""
                                    }
                                    if (relevantInfo) {
                                        promptParts << """Relevant user information:
---------------
$relevantInfo
---------------"""
                                    }
                                    promptParts << message
                                    def currentRequest = promptParts.join("\n\n")

                                    // Validation loop
                                    def counter = 0
                                    final MAX_COUNT = 3
                                    String reply = ''
                                    do {
                                        def answer = connector.ask(currentRequest)
                                        reply = answer

                                        println "$message : $reply"

                                        println 'Validating --------------------------------'
                                        def validationAnswer = validate(reply, message, currentPreferences, relevantInfo)
                                        println "Validation: $validationAnswer"

                                        if (validationAnswer.trim().startsWith('INVALID')) {
                                            currentRequest = "Please correct the response. Validation failed. Result: $validationAnswer"
                                            counter++
                                        } else {
                                            break
                                        }

                                    } while (counter < MAX_COUNT)

                                    if (counter >= MAX_COUNT) {
                                        throw new IllegalArgumentException("The supplied response is not valid: $reply")
                                    }

                                    // Return to EDT for Swing updates to show reply to user
                                    SwingUtilities.invokeLater {
                                        chatArea.append("assistant: ${reply}\n")
                                        chatArea.setCaretPosition(
                                                chatArea.document.length
                                        )
                                        sendButton.enabled = true
                                    }

                                    // Extract facts learned about the user from this iteration and save to file
                                    try {
                                        println 'Extracting new user info ------------------'
                                        infoExtractionConnector.clearMessages()
                                        def extractedFacts = infoExtractionConnector.ask("""User message:
$message

Assistant reply:
$reply""")
                                        if (extractedFacts && !extractedFacts.trim().equalsIgnoreCase('NONE') && !extractedFacts.trim().startsWith('NONE')) {
                                            saveLearntUserInfo(learntInfoFile, extractedFacts)
                                        } else {
                                            println 'No new user info learned.'
                                        }
                                    } catch (Exception extractErr) {
                                        println "Error extracting user info: ${extractErr.message}"
                                    }

                                } catch (Exception e) {
                                    SwingUtilities.invokeLater {
                                        chatArea.append(
                                                "assistant: [ERROR] ${e.message}\n"
                                        )
                                        sendButton.enabled = true
                                    }
                                }
                            }
                        }
                )
            }
        }
    }
    mainFrame.show()
}

// Retrieve relevant bits once per script run after the GUI is fully loaded
Thread.start {
    try {
        def currentPreferences = preferencesFile.exists() ? preferencesFile.text.trim() : preferences
        def allLearntInfo = learntInfoFile.exists() ? learntInfoFile.text.trim() : ''

        if (allLearntInfo || currentPreferences) {
            println 'Retrieving relevant user info -------------'
            relevantBitsConnector.clearMessages()
            def retrieved = relevantBitsConnector.ask("""User preferences:
${currentPreferences ?: 'None'}

Conversation history and learned user information:
${allLearntInfo ?: 'None'}

Please retrieve and consolidate the relevant bits of user information and preferences.""")

            if (retrieved && !retrieved.trim().equalsIgnoreCase('NONE') && !retrieved.trim().startsWith('NONE')) {
                relevantInfo = retrieved.trim()
                println "Relevant info found:\n$relevantInfo"
            } else {
                println 'No relevant user info found.'
            }
        }
    } catch (Exception e) {
        println "Error retrieving relevant bits: ${e.message}"
    } finally {
        SwingUtilities.invokeLater {
            mainFrame.title = 'LLM Personal Agent'
            swing.sendButton.enabled = true
        }
    }
}

void saveLearntUserInfo(File file, String facts) {
    String cleanFacts = facts.trim()
    if (!cleanFacts || cleanFacts.equalsIgnoreCase('NONE') || cleanFacts.startsWith('NONE')) {
        return
    }
    file.parentFile?.mkdirs()
    if (!file.exists()) {
        file.text = "# Learnt User Information\n\n"
    }
    String existingText = file.text
    boolean appended = false
    cleanFacts.eachLine { line ->
        String trimmed = line.trim()
        if (trimmed && !trimmed.equalsIgnoreCase('NONE') && !existingText.contains(trimmed)) {
            if (!trimmed.startsWith('-') && !trimmed.startsWith('*')) {
                trimmed = "- " + trimmed
            }
            file.append(trimmed + "\n")
            appended = true
        }
    }
    if (appended) {
        println "Learnt user info saved to ${file.name}:\n$cleanFacts"
    }
}

String validate(String response, String originalRequest, String preferences = '', String relevantInfo = '') {
    def contextSection = ''
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

    def validator = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: """
You validate whether the provided response correctly and appropriately addresses the user request.
${contextSection}
The original user request was: 
---------------
$originalRequest
---------------

Evaluate whether the response:
- Directly, accurately, and adequately addresses the user request.
- Fulfills any explicit constraints, instructions, or format requirements specified in the request.
- Adheres to and respects the user preferences and known user information.
- Is helpful, coherent, and free of contradictions, false claims, or irrelevant divergence.

You must respond with text starting with either 'CORRECT' or 'INVALID'. An detailed explanation of your decision should follow only if the response is INVALID.
    """)
    String answer = validator.ask(response)
    return answer
}