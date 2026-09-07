#!/usr/bin/env groovy
import helpers.LLMGenerateConnector

// Prompt for the model
def prompt = """
What is the capital of CZ?
"""

// Use the LLMGenerateConnector helper to generate the response
def connector = new LLMGenerateConnector(debug: false)
def rawOutput = connector.ask(prompt)

println "=== Raw model output ==="
println rawOutput
println "========================"