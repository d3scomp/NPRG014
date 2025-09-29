#!/usr/bin/env groovy
import groovy.json.JsonSlurper

// Prompt for the model
def prompt = """
Write a simple groovy script that generates an array of 10 integers with random values,
prints it, sorts it using bubble sort while printing the state of the array after each step
and finally prints the resulting sorted array.
"""

// Build request JSON
def requestBody = [
    model: "gemma3:4b",
    prompt: prompt,
    stream: false
]

// Open HTTP connection
def url = new URL("http://localhost:11434/api/generate")
def conn = url.openConnection()
conn.setRequestMethod("POST")
conn.doOutput = true
conn.setRequestProperty("Content-Type", "application/json")

// Send JSON body
conn.outputStream.withWriter("UTF-8") { writer ->
    writer << groovy.json.JsonOutput.toJson(requestBody)
}

// Read response
def responseText = conn.inputStream.getText("UTF-8")
def parsed = new JsonSlurper().parseText(responseText)
def rawOutput = parsed.response as String

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