#!/usr/bin/env groovy
import helpers.LLMGenerateConnector

// Prompt for the model
def prompt = """
Write a simple groovy script that generates an array of 10 integers with random values,
prints it, sorts it using bubble sort while printing the state of the array after each step
and finally prints the resulting sorted array.
"""

// Use the LLMGenerateConnector helper to generate the response
//def connector = new LLMGenerateConnector(debug: true)
//def connector = new LLMGenerateConnector(model: 'gemma4', debug: true)
//def connector = new LLMGenerateConnector(model: 'gemma4:12b', debug: true)
//def connector = new LLMGenerateConnector(model: 'gemma4:31b', debug: true)
def connector = new LLMGenerateConnector(model: 'qwen3.6', debug: true)
def rawOutput = connector.ask(prompt)

println "=== Raw model output ==="
println rawOutput
println "========================"

// Extract code block (```groovy ... ```)
def matcher = rawOutput =~ /```(?:groovy)?\s*([\s\S]*?)```/
def groovyCode = matcher ? matcher[0][1].trim() : rawOutput.trim()

println "=== Extracted Groovy code ==="
println groovyCode
println "============================="

// Run extracted code
def shell = new GroovyShell()
def script = shell.parse(groovyCode)
script.run()