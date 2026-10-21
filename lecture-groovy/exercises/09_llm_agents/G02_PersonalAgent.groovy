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

def connector = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: 'You are a helpful personal agent.')
def swing = new SwingBuilder()

swing.edt {
    frame(
            title: 'LLM Personal Agent',
            size: [640, 480],
            locationByPlatform: true
    ) {
        borderLayout()

        // Central area displaying the chat history
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

        // Bottom panel for user input
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

                            // Update the UI immediately with the user's message
                            chatArea.append("user: ${message}\n")
                            inputField.text = ''
                            sendButton.enabled = false

                            // Run the potentially slow LLM call in a background thread, off the EDT
                            Thread.start {
                                try {
                                    def currentRequest = message
                                    def counter = 0
                                    final MAX_COUNT = 3
                                    String reply = ''
                                    do {
                                        def answer = connector.ask(currentRequest)
                                        reply = answer

                                        println "$message : $reply"

                                        println 'Validating --------------------------------'
                                        def validationAnswer = validate(reply, message)
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

                                    // Switch back to the EDT thread for safe Swing UI updates
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

String validate(String response, String originalRequest) {
    def validator = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: """
You validate whether the provided response correctly and appropriately addresses the user request.
The original user request was: 
---------------
$originalRequest
---------------

Evaluate whether the response:
- Directly, accurately, and adequately addresses the user request.
- Fulfills any explicit constraints, instructions, or format requirements specified in the request.
- Is helpful, coherent, and free of contradictions, false claims, or irrelevant divergence.

You must respond with text starting with either 'CORRECT' or 'INVALID'. An detailed explanation of your decision should follow only if the response is INVALID.
    """)
    String answer = validator.ask(response)
    return answer
}