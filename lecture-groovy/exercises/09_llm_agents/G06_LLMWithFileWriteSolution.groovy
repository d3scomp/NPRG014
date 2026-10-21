#!/usr/bin/env groovy

import helpers.*
import groovy.swing.SwingBuilder
import groovy.transform.Field

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


// ── Prompt Templates ─────────────────────────────────────────────────────────

@Field final String PROMPT_MAIN_AGENT = 'You are a helpful personal agent.'

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
    "write_file",
    "Writes content to a file. Only files within the allowed project directory can be written.",
    [
        type: "object",
        properties: [
            path: [
                type: "string",
                description: "The absolute or relative path to the file to write."
            ],
            content: [
                type: "string",
                description: "The content to write to the file."
            ]
        ],
        required: ["path", "content"]
    ]
) { args ->
    return write_file(args.path as String, args.content as String)
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
        return "System Error: The 'path' argument cannot be null or empty."
    }

    File targetFile = new File(path)

    // Agent self-correction aid: Resolve the absolute path for error reporting
    // so the LLM knows exactly where the system was looking.
    String absolutePath = targetFile.absolutePath

    if (!targetFile.exists()) {
        return "System Error: File not found. No file exists at path: ${absolutePath}. Please verify the directory contents or path."
    }

    if (targetFile.isDirectory()) {
        return "System Error: The provided path points to a directory, not a file: ${absolutePath}. Please provide a path to a specific file, or use a directory listing tool."
    }

    // Groovy's .text property safely handles opening, reading, and closing the stream,
    // returning the full multi-line string content.
    return targetFile.text
}

/**
 * Writes content to a file.
 * Designed for MCP tool invocation by LLM agents.
 *
 * The file can only be written if its canonical absolute path starts with
 * /Users/vaclav/work/mff/NPRG014/.
 *
 * @param path The absolute or relative path to the file.
 * @param content The content to write to the file.
 * @return A confirmation message.
 * @throws IllegalArgumentException if the path is invalid, outside the allowed directory, or a directory.
 */
String write_file(String path, String content) {
    if (path == null || path.trim().isEmpty()) {
        return "System Error: The 'path' argument cannot be null or empty."
    }

    File targetFile = new File(path)

    // Resolve the canonical path to prevent symlink-based directory traversal
    String canonicalPath = targetFile.canonicalPath

    // Security check: restrict writes to the allowed project directory (absolute path)
    if (!canonicalPath.startsWith('<path to the project repository>/lecture-groovy/exercises/')) {
        return "Write not allowed. Path ${canonicalPath} is outside the allowed directory."
    }

    // Ensure parent directories exist
    targetFile.parentFile?.mkdirs()

    // Groovy's .text property safely handles opening, writing, and closing the stream
    targetFile.text = content

    return "File written successfully: ${canonicalPath}"
}

def handleUserMessage(String message) {
        println('Asking main LLM -------------------------------')

        /*
         * The new LLMChatConnector internally orchestrates the 
         * agent loop, intercepts tool execution requests, calls the
         * registered groovy closure, and sends the results back to the LLM.
         */
        String answer = connector.ask(message)

        return answer
}

// TASK 1 Specify the directory <path to the project repository> above and below (absolute or relative) and observe the behavior
// TASK 2 Secure the write_file tool so that it can only write to 
//        the <path to the project repository>/lecture-groovy/exercises/ directory
//        Delete the welcome.txt and not_welcome.txt files
//        Observe the behavior. Writing to not_welcome.txt should be forbidden

def result1 = handleUserMessage("""
   Write the text 'I was here, Fantomas!' into the `welcome.txt` file in `<path to the project repository>/lecture-groovy/exercises/`.
""")
println result1

def result2 = handleUserMessage("""
   Write the text 'I was here, Fantomas!' into the `not_welcome.txt` file in `<path to the project repository>/lecture-groovy/`.
""")
println result2

println 'well done'