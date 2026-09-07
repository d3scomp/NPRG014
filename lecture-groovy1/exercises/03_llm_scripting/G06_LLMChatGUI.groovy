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

def connector = new LLMChatConnector(debug: false, systemPrompt: 'You only use upper-case letters in your replies.')
def swing = new SwingBuilder()

swing.edt {
    frame(
            title: 'Demo LLM Chat',
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
                                    def reply = connector.ask(message)

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