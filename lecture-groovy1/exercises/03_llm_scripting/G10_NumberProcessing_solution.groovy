#!/usr/bin/env groovy
import helpers.*

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
You are a coding assistant specialized in querying Groovy collections using the Groovy collections API - e.g. collect, findAll, inject, each, etc.
All your responses must be valid idiomatic Groovy code.
More specifically, you always return code that represents a single operation on the `numbers` List of integers that is implicitly available as a local variable.
For example, `numbers.findAll {it > 5}` is a possible output for a request 'Must be bugger than 5'.
''')

def currentNumbers = numbers.clone()
userInput.each {userRequest ->
    def counter = 0
    final MAX_COUNT = 3
    def codeToRun = ''
    def question = userRequest
    do {
        def answer = connector.ask(question)
    
        // EXTRACT CODE FROM MARKDOWN
        codeToRun = answer
        def matcher = answer =~ /(?s)```(?:groovy)?\s*(.*?)```/
        if (matcher.find()) {
            codeToRun = matcher.group(1).trim()
        }
    
        println "$userRequest : $codeToRun"
        println "Validating"
        
        def validation = isValid(codeToRun)
        println validation
        
        if (validation.startsWith('INVALID')) {
            question = "Please correct the code. Validation failed. Result: $validation"
            counter++
        } else {
            break
        }

    } while (counter < MAX_COUNT)

    if (counter >= MAX_COUNT) {
        throw new IllegalArgumentException("The supplied code is not valid: $codeToRun")
    }
    

    def binding = new Binding()
    binding.numbers = currentNumbers
    def shell = new GroovyShell(binding)
    currentNumbers = shell.evaluate(codeToRun)
    println currentNumbers
}

String isValid(String code) {
    def validator = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: '''
    You validate whether the provided code is valid Groovy method call on the `numbers` receiver object.
    More specifically, you ensure that the provided code snippet represent a single operation on the `numbers` List that is implicitly available as a local variable.
    For example, `numbers.findAll {it > 5}` is a correct code.
    No variables must be defined in the code. No println is allowed. Just a single dot operation calling a method on `numbers`.
    Cases, when the `collect` method unlike `findAll` is used on a boolean predicate, e.g. in `numbers.collect { it > 12 || it < 5 }`, must be rejected.
    You must respond with text starting with either 'CORRECT' or 'INVALID'. An detailed explanation of your decision should follow only if the code is INVALID.
    ''')
    String answer = validator.ask(code)
    return answer
}