#!/usr/bin/env groovy

// TASK: Give me a number between 1 and 10.

import helpers.*
import groovy.swing.SwingBuilder
import groovy.transform.Field

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


// ── Prompt Templates ─────────────────────────────────────────────────────────

@Field final String PROMPT_MAIN_AGENT = 'You are a helpful personal agent. You reply as briefly as possible. Ideally with a single word or single a number.'

// ── LLM Connectors ───────────────────────────────────────────────────────────
/*
 * MAIN LLM
 *
 * Generates the actual answer to the user's request.
 */
@Field def connector = new LLMChatConnectorWithTools(
    debug: false,
    model: 'gemma4',
    systemPrompt: PROMPT_MAIN_AGENT
)

/*
 * REGISTER MCP TOOLS
 *
 * Registering the tool on the connector. The enhanced LLMChatConnector will 
 * handle the execution of this closure automatically during its internal loop.
 */
connector.registerTool(
    "list_directories",
    "Lists the contents of a directory, returning a multi-line string with the file type (DIR/FILE), UTC updated timestamp, and file name.",
    [
        type: "object",
        properties: [
            targetPath: [
                type: "string",
                description: "The absolute or relative path of the directory to list"
            ]
        ],
        required: ["targetPath"]
    ]
) { args ->
    return list_directories(args.targetPath as String)
}
connector.registerTool(
    "read_file",
    "Reads the content of a file and returns it as a multi-line string. Both absolute and relative paths are allowed.",
    [
        type: "object",
        properties: [
            path: [
                type: "string",
                description: "The absolute or relative path to the file to read."
            ]
        ],
        required: ["path"]
    ]
) { args ->
    return read_file(args.path as String)
}
connector.registerTool(
    "random_number",
    """
    Returns a truly random number between from and to values (including)
    Use whenever asked for a random number.
    Prefer using this tool to guessing it yourself.
    """,
    [
        type: "object",
        properties: [
            from: [
                type: "string",
                description: "The lower boundary."
            ],
            to: [
                type: "string",
                description: "The higher boundary."
            ]            
        ],
        required: ["from", "to"]
    ]
) { args ->
    return random_number(args.from as String, args.to as String)
}

String list_directories(String targetPath) {
    Path path = Paths.get(targetPath).toAbsolutePath().normalize()

    // 1. Verify existence and type
    if (!Files.exists(path)) {
        return "ERROR: Path does not exist - ${path}"
    }
    if (!Files.isDirectory(path)) {
        return "ERROR: Path is not a directory - ${path}"
    }

    StringBuilder output = new StringBuilder()
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC)

    // 2. Provide a clear schema header for LLM context
    output.append("TYPE\tUPDATED\tNAME\n")

    try {
        // 3. Stream directory contents, ensure closure to prevent resource leaks
        Files.list(path).withCloseable { stream ->
            stream.sorted().each { child ->
                BasicFileAttributes attrs = Files.readAttributes(child, BasicFileAttributes.class)
                String type = attrs.isDirectory() ? "DIR" : "FILE"
                String updated = formatter.format(attrs.lastModifiedTime().toInstant())
                
                output.append("${type}\t${updated}\t${child.fileName}\n")
            }
        }
    } catch (Exception e) {
        return "ERROR: Failed to read directory contents - ${e.message}"
    }

    return output.toString().trim()
}

/**
 * Reads the content of a file and returns it as a multi-line string.
 * Designed for MCP tool invocation by LLM agents.
 *
 * @param path The absolute or relative path to the file.
 * @return The contents of the file as a String.
 * @throws IllegalArgumentException if the path is invalid, missing, or a directory.
 */
String read_file(String path) {
    if (path == null || path.trim().isEmpty()) {
        throw new IllegalArgumentException("System Error: The 'path' argument cannot be null or empty.")
    }

    File targetFile = new File(path)

    // Agent self-correction aid: Resolve the absolute path for error reporting
    // so the LLM knows exactly where the system was looking.
    String absolutePath = targetFile.absolutePath

    if (!targetFile.exists()) {
        throw new IllegalArgumentException("System Error: File not found. No file exists at path: ${absolutePath}. Please verify the directory contents or path.")
    }

    if (targetFile.isDirectory()) {
        throw new IllegalArgumentException("System Error: The provided path points to a directory, not a file: ${absolutePath}. Please provide a path to a specific file, or use a directory listing tool.")
    }

    // Groovy's .text property safely handles opening, reading, and closing the stream,
    // returning the full multi-line string content.
    return targetFile.text
}

String random_number(String from, String to) {
    if (from == null || from.trim().isEmpty()) {
        return "System Error: The 'from' argument cannot be null or empty."
    }
    if (to == null || to.trim().isEmpty()) {
        return "System Error: The 'to' argument cannot be null or empty."
    }
    int fromNum = from as Integer
    if (fromNum == null) {
        return "The 'from' argument cannot parsed to a number."
    }
    int toNum = to as Integer    
    if (toNum == null) {
        return "The 'to' argument cannot parsed to a number."
    }
    
    return new Random().nextInt(toNum+1) + fromNum as String
}

def handleUserMessage(String message) {
        connector.clearMessages()
        println('Asking main LLM -------------------------------')

        /*
         * The new LLMChatConnector internally orchestrates the 
         * agent loop, intercepts tool execution requests, calls the
         * registered groovy closure, and sends the results back to the LLM.
         */
        String answer = connector.ask(message)

        return answer

}

def results = (1..3).collect {
    handleUserMessage("Give me a number between 1 and 10")
}
println (results)
assert results.any {it != '7'}
println 'well done'
