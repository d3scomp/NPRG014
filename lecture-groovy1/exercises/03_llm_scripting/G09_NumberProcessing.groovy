#!/usr/bin/env groovy
import helpers.*
/*
 This script iteratively transforms the `numbers` collection based on the plain English instructions in `userInput`.
 An LLM is used to translate each instruction into executable Groovy code.
 The generated Groovy code is then evaluated dynamically in a GroovyShell to apply the transformation.
 
 TASK 1: Configure the main LLMChatConnector's system prompt with clear instructions. 
         Hint: Providing examples of the desired output often works better than lengthy explanations.
 TASK 2: Enhance the GroovyShell Binding so the generated scripts can access the numbers collection.
*/

final numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 23, 27, 101, 208, 513, 1023]

//A test user script input.
List<String> userInput = [
                            'Must be bigger than 12 or smaller than 5',
                            'Must be odd',
                            'Multiply it by 2',
                            'Must be divisible by 5',
                            'Summarize squares of all the numbers'
                            ]


// Use the LLMChatConnector helper for a conversation
def connector = new LLMChatConnector(debug: false, systemPrompt: '''
You are a coding assistant.
''')


def currentNumbers = numbers.clone()
userInput.each {
    def answer = connector.ask(it)

    // EXTRACT CODE FROM MARKDOWN
    String codeToRun = answer
    def matcher = answer =~ /(?s)```(?:groovy)?\s*(.*?)```/
    if (matcher.find()) {
        codeToRun = matcher.group(1).trim()
    }

    println "$it : $codeToRun"
    
    def binding = new Binding()
    def shell = new GroovyShell(binding)
    currentNumbers = shell.evaluate(codeToRun)
    println currentNumbers
}
