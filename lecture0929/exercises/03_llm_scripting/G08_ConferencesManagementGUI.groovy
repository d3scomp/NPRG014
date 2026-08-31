#!/usr/bin/env groovy

import helpers.*
import groovy.swing.SwingBuilder
import groovy.lang.GroovyShell
import groovy.lang.Binding

import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.BoxLayout
import javax.swing.SwingUtilities
import javax.swing.border.EmptyBorder

import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Component

class Event {
    String name
    String location
    String topic
    
    @Override
    String toString() {
        return "Event {\n" +
               "  Name     : '" + name + "'\n" +
               "  Location : '" + location + "'\n" +
               "  Topic    : '" + topic + "'\n" +
               "}"
    }    
}

def conferences = [
    new Event(name: "JavaOne", location: "San Francisco", topic: "Java"),
    new Event(name: "AWS re:Invent", location: "Las Vegas", topic: "Cloud Computing"),
    new Event(name: "Google I/O", location: "Mountain View", topic: "Software Development"),
    new Event(name: "NeurIPS", location: "Vancouver", topic: "Artificial Intelligence"),
    new Event(name: "WWDC", location: "Cupertino", topic: "Apple Ecosystem"),
    new Event(name: "Microsoft Build", location: "Seattle", topic: "Software Development"),
    new Event(name: "PyCon US", location: "Pittsburgh", topic: "Python"),
    new Event(name: "KubeCon + CloudNativeCon", location: "Chicago", topic: "Cloud Native"),
    new Event(name: "QCon", location: "London", topic: "Software Architecture"),
    new Event(name: "Devoxx", location: "Antwerp", topic: "Java and Web Technologies"),
    new Event(name: "DEF CON", location: "Las Vegas", topic: "Cybersecurity")
]

def connector = new LLMChatConnector(debug: false, systemPrompt: '''
You are a coding assistant. All your responses must be valid idiomatic Groovy code, unless asked otherwise.
The context contains a variable named `listOfConferences`, which is a List of Event instances:
class Event {
    String name
    String location
    String topic
}
You handle requests by providing code that queries the `listOfConferences` List using the Groovy collections API - e.g. collect, findAll, inject, each, etc.
''')
def swing = new SwingBuilder()

def msgFont = new Font('Monospaced', Font.PLAIN, 13)

swing.edt {
    frame(
            id: 'mainFrame',
            title: 'Demo LLM Chat',
            size: [640, 480],
            locationByPlatform: true
    ) {
        borderLayout()

        // Central chat history display — a vertical box inside a scroll pane
        scrollPane(
                id: 'chatScroll',
                constraints: BorderLayout.CENTER,
                verticalScrollBarPolicy: JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        ) {
            // Wrapper panel to ensure top-down growth
            panel(background: new Color(245, 245, 245)) {
                borderLayout()
                
                panel(
                        id: 'chatPanel',
                        constraints: BorderLayout.NORTH,
                        background: new Color(245, 245, 245)
                ) {
                    boxLayout(axis: BoxLayout.Y_AXIS)
                }
            }
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

                            // Append user message row
                            SwingUtilities.invokeLater {
                                def userText = swing.textArea(
                                        text: "user: ${message}",
                                        font: msgFont,
                                        editable: false,
                                        opaque: false, // Makes background transparent to match panel
                                        lineWrap: true,
                                        wrapStyleWord: true,
                                        alignmentX: Component.LEFT_ALIGNMENT,
                                        border: new EmptyBorder(0, 0, 8, 0)
                                )
                                chatPanel.add(userText)
                                chatPanel.revalidate()
                                chatPanel.repaint()
                            }

                            inputField.text = ''
                            sendButton.enabled = false

                            // Do the potentially slow LLM call off the EDT
                            Thread.start {
                                try {
                                    def reply = connector.ask(message)

                                    // Return to EDT for Swing updates
                                    SwingUtilities.invokeLater {
                                        // Build a row: text label + clickable code icon
                                        // Added left alignment to prevent dragging other components to center
                                        def row = swing.panel(border: new EmptyBorder(0, 0, 8, 0), opaque: false, alignmentX: Component.LEFT_ALIGNMENT) {
                                            // FIX: use named arguments for gap properties
                                            borderLayout(hgap: 8, vgap: 0) 
                                            
                                            button(
                                                    constraints: BorderLayout.WEST, // Moved to front
                                                    text: '▶',
                                                    font: new Font('SansSerif', Font.BOLD, 14),
                                                    cursor: java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR),
                                                    toolTipText: 'Run as Groovy code',
                                                    borderPainted: false,
                                                    contentAreaFilled: false,
                                                    focusPainted: false,
                                                    actionPerformed: {
                                                        // Evaluate the assistant's text as Groovy code
                                                        try {
                                                            // EXTRACT CODE FROM MARKDOWN
                                                            String codeToRun = reply
                                                            def matcher = reply =~ /(?s)```(?:groovy)?\s*(.*?)```/
                                                            if (matcher.find()) {
                                                                codeToRun = matcher.group(1).trim()
                                                            }

                                                            // Prepare the environment and run the code
                                                            def binding = new Binding()
                                                            binding.listOfConferences = conferences
                                                            def shell = new GroovyShell(binding)
                                                            def result = shell.evaluate(codeToRun)
                                                            
                                                            println "Result: $result"
                                                        } catch (Exception runErr) {
                                                            println "Groovy execution error: ${runErr.message}"
                                                            runErr.printStackTrace()
                                                        }
                                                    }
                                            )
                                            
                                            textArea(
                                                    constraints: BorderLayout.CENTER,
                                                    text: "assistant:\n${reply}",
                                                    font: msgFont,
                                                    editable: false,
                                                    opaque: false,
                                                    lineWrap: true,
                                                    wrapStyleWord: true
                                            )
                                        }
                                        chatPanel.add(row)
                                        chatPanel.revalidate()
                                        chatPanel.repaint()

                                        def vsb = chatScroll.verticalScrollBar
                                        vsb.setValue(vsb.minimum + vsb.maximum - vsb.visibleAmount)

                                        sendButton.enabled = true
                                    }
                                } catch (Exception e) {
                                    SwingUtilities.invokeLater {
                                        def errText = swing.textArea(
                                                text: "assistant: [ERROR] ${e.message}",
                                                font: msgFont,
                                                foreground: Color.RED,
                                                editable: false,
                                                opaque: false,
                                                lineWrap: true,
                                                wrapStyleWord: true,
                                                alignmentX: Component.LEFT_ALIGNMENT,
                                                border: new EmptyBorder(0, 0, 8, 0)
                                        )
                                        chatPanel.add(errText)
                                        chatPanel.revalidate()
                                        chatPanel.repaint()
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