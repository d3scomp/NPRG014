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

def connector = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: 'You are a helpful personal agent.')
def swing = new SwingBuilder()

swing.edt {
    frame(
            title: 'LLM Personal Agent',
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
                                    def currentRequest = currentPreferences ? """User preferences:
---------------
$currentPreferences
---------------

$message""" : message

                                    def counter = 0
                                    final MAX_COUNT = 3
                                    String reply = ''
                                    do {
                                        def answer = connector.ask(currentRequest)
                                        reply = answer

                                        println "$message : $reply"

                                        println 'Validating --------------------------------'
                                        def validationAnswer = validate(reply, message, currentPreferences)
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

                                    // Return to EDT for Swing updates
                                    SwingUtilities.invokeLater {
                                        chatArea.append("assistant: ${reply}\n")
                                        chatArea.setCaretPosition(
                                                chatArea.document.length
                                        )
                                        sendButton.enabled = true
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
    }.show()
}

String validate(String response, String originalRequest, String preferences = '') {
    def preferencesSection = preferences ? """
User preferences:
---------------
$preferences
---------------
""" : ''

    def validator = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: """
You validate whether the provided response correctly and appropriately addresses the user request.
${preferencesSection}
The original user request was: 
---------------
$originalRequest
---------------

Evaluate whether the response:
- Directly, accurately, and adequately addresses the user request.
- Fulfills any explicit constraints, instructions, or format requirements specified in the request.
- Adheres to and respects the user preferences.
- Is helpful, coherent, and free of contradictions, false claims, or irrelevant divergence.

You must respond with text starting with either 'CORRECT' or 'INVALID'. An detailed explanation of your decision should follow only if the response is INVALID.
    """)
    String answer = validator.ask(response)
    return answer
}